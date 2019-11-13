package jandcode.core.jsa.jc

import groovy.json.*
import jandcode.commons.*
import jandcode.commons.moduledef.*
import jandcode.commons.named.*
import jandcode.jc.*

/**
 * Утилитки всякие
 */
class JsaUtils extends ProjectScript {

    /**
     * Возвращает список jsa-модулей для проекта.
     *
     * @return список , в котором только JsaModule. Отсортирован по depends: сначала
     * идут зависимые, потом зависящие.
     */
    public NamedList<JsaModule> getJsaModules() {
        return ctx.service(JsaService).getJsaModules(project)
    }

    /**
     * Расчет хеша, который постоянен, если нет изменений в модулях проекта
     * или самого проекта.
     */
    String calcJsaHash() {
        List<JsaModule> mods = getJsaModules()
        String hash = ''

        def pi = { Project p ->
            return p.projectFile +
                    '|' + new File(p.projectFile).lastModified() +
                    '|' + new File(p.wd()).lastModified() +
                    '|'
        }

        def piJar = { Lib p ->
            return p.jar +
                    '|' + new File(p.jar).lastModified() +
                    '|'
        }

        for (JsaModule m in mods) {
            if (m.lib.sourceProject == null) {
                hash += piJar(m.lib)
            } else {
                hash += pi(m.lib.sourceProject)
            }
        }
        hash += pi(project)

        hash = UtString.md5Str(hash)
        return hash
    }

    /**
     * Возвращает полный путь до исполняемого файла node
     * @return
     */
    String getNodeExe() {
        return ut.findcmd(cmd: "node", warn: true)
    }

    /**
     * Возвращает версию node
     * @param nodeExe исполняемый файл node или null для автоопределения
     * @return пустую строку, если не получилось
     */
    String getNodeVersion(String nodeExe = null) {
        if (!nodeExe) {
            nodeExe = getNodeExe()
        }
        if (!nodeExe) {
            return ""
        }
        try {
            List<String> res = ut.runcmd(cmd: "${nodeExe} -v", saveout: true, showout: false)
            if (res.size() > 0) {
                return res[0].replace('v', '')
            }
        } catch (Exception e) {
            log.warn(e)
            return ""
        }
    }

    /**
     * Генерить jc-env.bat для проекта.
     */
    String makeJcEnvBat() {
        // все библиотеки
        List<JsaModule> moduleInfos = getJsaModules()

        //
        StringBuilder sb = new StringBuilder()

        String v

        def addPath = { String vn, String p ->
            String absP = UtFile.abs(p)
            if (UtFile.isWindows()) {
                sb.append("set ${vn}=%${vn}%;${absP}\n")
            } else {
                sb.append("${vn}=\${${vn}}:${absP}\n")
            }
        }

        // NODE_PATH
        v = "JSA_NODE_PATH"
        if (UtFile.isWindows()) {
            sb.append("set ${v}=\n")
        } else {
            sb.append("export ${v}=\n")
        }
        for (int i = moduleInfos.size() - 1; i >= 0; i--) {
            JsaModule m = moduleInfos.get(i)
            for (String p1 : m.getResolvePaths()) {
                addPath(v, p1)
            }
        }
        addPath(v, wd(JsaConsts.NODE_MODULES))
        addPath(v, wd(JcConsts.JC_METADATA_DIR))

        if (UtFile.isWindows()) {
            sb.append("set NODE_PATH=%${v}%;%NODE_PATH%\n")
        } else {
            sb.append("export NODE_PATH=\${${v}}:\${NODE_PATH}\n")
        }

        // PATH
        v = "JSA_PATH"
        if (UtFile.isWindows()) {
            sb.append("set ${v}=\n")
        } else {
            sb.append("export ${v}=\n")
        }
        addPath(v, wd("${JsaConsts.NODE_MODULES}/.bin"))
        if (UtFile.isWindows()) {
            sb.append("set PATH=%${v}%;%PATH%\n")
            sb.append("set NODE_OPTIONS=%NODE_OPTIONS% -r %~dp0${JsaConsts.JSA_MODULES}\n")
        } else {
            sb.append("PATH=\${${v}}:\${PATH}\n")
            sb.append("export NODE_OPTIONS=\"\${NODE_OPTIONS} -r \${WD}/${JsaConsts.JSA_MODULES}\"\n")
        }

        return sb.toString()
    }

    /**
     * Генерить jsa-webpack-dummy-config.js
     */
    String makeJsaWebpackDummy() {

        List<JsaModule> moduleInfos = getJsaModules()

        String modules = ''
        String aliases = ''

        def norm = { String s ->
            return s.replace('\\', '/').replace('\'', '\\\'')
        }

        String indent = ' ' * 12
        for (int i = moduleInfos.size() - 1; i >= 0; i--) {
            JsaModule mi = moduleInfos.get(i)
            for (p in mi.resolvePaths) {
                modules += "${indent}'${norm(p)}',\n"
            }
        }
        modules += "${indent}'${norm(wd(JsaConsts.NODE_MODULES))}',\n"
        modules += "${indent}'${norm(wd(JcConsts.JC_METADATA_DIR))}',\n"

        for (int i = moduleInfos.size() - 1; i >= 0; i--) {
            JsaModule mi = moduleInfos.get(i)
            ModuleDef md = mi.moduleDef
            aliases += "${indent}'${md.name}': '${norm(UtFile.vfsPathToLocalPath(md.path))}',\n"
        }

        String template = """// THIS FILE GENERATED, NOT MODIFY!
const path = require('path');
module.exports = {
    entry: './${JsaConsts.WEBPACK_DUMMY_ENTRY}',

    output: {
        path: path.resolve(__dirname, '../temp'),
        filename: 'build-${JsaConsts.WEBPACK_DUMMY_ENTRY}'
    },

    resolve: {                          
        modules: [
${modules}            
        ],
        alias: {
${aliases}        
        }
    }                      
};                                   
"""
        return template
    }

    /**
     * Генерить jsa-modules.js
     */
    String makeJsaModulesJs() {
        List<JsaModule> moduleInfos = getJsaModules()

        def norm = { String s ->
            return s.replace('\\', '/').replace('\'', '\\\'')
        }

        StringBuilder sb = new StringBuilder()

        sb.append("// THIS FILE GENERATED, NOT MODIFY!\n")
        sb.append("let ma = require('./jsa-prepare-data/module-alias')\n")
        sb.append("let z = {}\n")
        sb.append("let m\n")
        sb.append("z.rootProjectName = '${project.name}'\n")
        sb.append("z.modules = []\n")
        sb.append("z.modulesByName = {}\n")

        for (int i = moduleInfos.size() - 1; i >= 0; i--) {
            JsaModule mi = moduleInfos.get(i)

            sb.append("//\n")
            sb.append("m = {}\n")
            sb.append("m.name = '${mi.moduleDef.name}'\n")
            sb.append("m.libName = '${mi.name}'\n")
            sb.append("m.pakPath = '${mi.moduleDef.name.replace('.', '/')}'\n")
            sb.append("m.srcPath = '${norm(mi.srcPath)}'\n")
            sb.append("m.isSource = ${mi.lib.sourceProject != null}\n")

            sb.append("m.nodeDepends = ${JsaUtJson.toJson(mi.getNodeDepends())}\n")
            sb.append("m.gulpTasks = ${JsaUtJson.toJson(ctx.service(JsaService).getGulpTasks(mi))}\n")

            sb.append("z.modules.push(m)\n")
            sb.append("z.modulesByName['${mi.moduleDef.name}'] = m\n")
            sb.append("ma.addAlias(m.name, m.srcPath + '/' + m.pakPath)\n")
        }

        //
        sb.append("//\n")
        sb.append("z.resolvePaths = []\n")
        sb.append("z.resolvePaths.push('${norm(wd(JcConsts.JC_METADATA_DIR))}')\n")
        sb.append("z.resolvePaths.push('${norm(wd(JsaConsts.NODE_MODULES))}')\n")
        sb.append("for (let rp of z.resolvePaths) {ma.addPath(rp)}\n")
        //
        sb.append("//\n")
        sb.append("module.exports = z\n")

        return sb.toString()
    }

    /**
     * Генерить package.json
     */
    String makePackageJson() {
        Map res = [:]

        res['name'] = project.name
        res['private'] = "true"

        res['dependencies'] = ctx.service(JsaService).getNodeDepends(project)

        String s = JsaUtJson.toJson(res)
        s = JsonOutput.prettyPrint(s)
        return s
    }

    /**
     * Обновить package.json текущим составом библиотек
     */
    void updatePackageJson(String fn) {
        if (!UtFile.exists(fn)) {
            String s = makePackageJson()
            UtFile.saveString(s, new File(fn))
            return
        }

        Map m
        String s = UtFile.loadString(fn)
        try {
            m = (Map) JsaUtJson.fromJson(s)
        } catch (e) {
            s = makePackageJson()
            UtFile.saveString(s, new File(fn))
            return
        }

        Map newDeps = ctx.service(JsaService).getNodeDepends(project)
        def oldDeps = ctx.service(JsaService).sortDependsMap(m['dependencies'])

        if (oldDeps.toString() != newDeps.toString()) {
            String tmpFile = wd("temp/${UtDateTime.now().toString(UtDateTime.createFormatter("yyyy-MM-dd--HHmmss"))}--dependencies.json")
            log.warn "Состав dependencies в ${fn} изменился. Предыдущий вариант в ${tmpFile}"
            UtFile.saveString(JsonOutput.prettyPrint(JsaUtJson.toJson([dependencies: oldDeps])), new File(tmpFile))

            // новый вариант
            m['dependencies'] = ctx.service(JsaService).getNodeDepends(project)
            s = JsaUtJson.toJson(m)
            s = JsonOutput.prettyPrint(s)

            UtFile.saveString(s, new File(fn))
        }

    }

    /**
     * Обновить package.json текущим составом библиотек
     */
    void updateNodeModules(String packageJsonFile) {
        String packageJsonPath = UtFile.path(packageJsonFile)

        def calcHash = {
            String hash = packageJsonFile +
                    '|' + new File(packageJsonFile).lastModified()
            return UtString.md5Str(hash)
        }

        def calcHashFile = {
            return UtFile.join(packageJsonPath, "node_modules/.bin/_jc/hash", "package.json-" + calcHash())
        }

        String hashFile = calcHashFile()
        if (UtFile.exists(hashFile)) {
            return
        }
        //
        ut.runcmd(cmd: "npm install", dir: packageJsonPath)

        // новый hash, ибо npm может запросто поменять package.json
        hashFile = calcHashFile()

        ant.echo(message: "", file: hashFile)
    }

}
