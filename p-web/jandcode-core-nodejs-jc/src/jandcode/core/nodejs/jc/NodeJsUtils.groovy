package jandcode.core.nodejs.jc


import jandcode.commons.*
import jandcode.commons.named.*
import jandcode.jc.*

class NodeJsUtils extends ProjectScript {

    static String PATH_BASE = JcConsts.JC_METADATA_DIR

    static String PATH_NM_HOLDER = "nodejs-nm-holder"
    static String PATH_NODE_MODULES = "${PATH_NM_HOLDER}/node_modules"
    static String PATH_NODE_MODULES_BIN = "${PATH_NODE_MODULES}/.bin"
    static String PATH_PACKAGE_JSON = "${PATH_NM_HOLDER}/package.json"
    static String PATH_PREPARE = "nodejs-prepare"
    static String FILE_JC_NODEJS_MODULES = "jc-nodejs-modules.js"
    static String PATH_JC_NODEJS_MODULES = "${PATH_PREPARE}/${FILE_JC_NODEJS_MODULES}"
    static String PATH_WEBPACK_DUMMY = "${PATH_PREPARE}/webpack-dummy.config.js"
    static String FILE_WEBPACK_DUMMY_ENTRY = "webpack-dummy-entry.js"
    static String PATH_WEBPACK_DUMMY_ENTRY = "${PATH_PREPARE}/${FILE_WEBPACK_DUMMY_ENTRY}"

    /**
     * Путь внутри каталога со средой
     */
    String getMetaDataPath(String path = "") {
        return UtFile.join(wd(PATH_BASE), path)
    }

    /**
     * Все модули, которые известны
     */
    NamedList<NodeJsModule> getAllModules() {
        NodeJsService svc = ctx.service(NodeJsService)
        NamedList<NodeJsModule> mods = svc.getModules()
        return mods
    }

    /**
     * Собирает все зависимости для формирования общего файла package.json
     * @return
     */
    Map grabAllDepends() {
        NamedList<NodeJsModule> mods = getAllModules()
        //
        def res = [:]

        for (m in mods) {
            def modDeps = m.getAllDependencies()
            for (md in modDeps) {
                String depName = md.key
                String depVersion = md.value
                if (mods.find(depName) != null) {
                    // это модуль, есть исходники, зависимость игнорируем
                    continue
                }
                res.put(depName, depVersion)
            }
        }

        return sortDependsMap(res)
    }

    /**
     * Генерить package.json
     */
    Map makeCommonPackageJson() {
        Map res = [:]

        res['name'] = project.name
        res['private'] = true

        res['dependencies'] = grabAllDepends()

        return res
    }

    /**
     * Генерить jc-env.bat для проекта.
     */
    String makeJcEnvBat() {
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
        v = "JC_NODEJS_NODE_PATH"
        if (UtFile.isWindows()) {
            sb.append("set ${v}=\n")
        } else {
            sb.append("export ${v}=\n")
        }
        addPath(v, getMetaDataPath(PATH_NODE_MODULES))
        addPath(v, getMetaDataPath(PATH_PREPARE))

        if (UtFile.isWindows()) {
            sb.append("set NODE_PATH=%${v}%;%NODE_PATH%\n")
        } else {
            sb.append("export NODE_PATH=\${${v}}:\${NODE_PATH}\n")
        }

        // PATH
        v = "JC_NODEJS_PATH"
        if (UtFile.isWindows()) {
            sb.append("set ${v}=\n")
        } else {
            sb.append("export ${v}=\n")
        }
        addPath(v, getMetaDataPath(PATH_NODE_MODULES_BIN))

        if (UtFile.isWindows()) {
            sb.append("set PATH=%${v}%;%PATH%\n")
            sb.append("set NODE_OPTIONS=%NODE_OPTIONS% -r ${getMetaDataPath(PATH_JC_NODEJS_MODULES)}\n")
        } else {
            sb.append("PATH=\${${v}}:\${PATH}\n")
            sb.append("export NODE_OPTIONS=\"\${NODE_OPTIONS} -r ${getMetaDataPath(PATH_JC_NODEJS_MODULES)}\"\n")
        }

        return sb.toString()
    }

    /**
     * Генерить jc-nodejs-modules.js
     */
    String makeJcNodejsModulesJs() {
        def mods = getAllModules()

        def norm = { String s ->
            return s.replace('\\', '/').replace('\'', '\\\'')
        }

        StringBuilder sb = new StringBuilder()

        sb.append("""\
//
// THIS FILE GENERATED, NOT MODIFY!
//
let ma = require('./module-alias')
let z = {}
let m
z.modules = []
z.modulesByName = {}
z.modulesPaths = {}
z.resolvePaths = []
""")

        for (mod in mods) {
            sb.append("//\n")
            sb.append("m = {}\n")
            sb.append("m.name = '${mod.name}'\n")
            sb.append("m.srcPath = '${norm(mod.path)}'\n")
            sb.append("z.modules.push(m)\n")
        }

        //
        sb.append("//\n")
        sb.append("z.resolvePaths.push('${norm(getMetaDataPath(PATH_PREPARE))}')\n")
        sb.append("z.resolvePaths.push('${norm(getMetaDataPath(PATH_NODE_MODULES))}')\n")
        //
        sb.append("""\
//
for (let m of z.modules) {
  z.modulesPaths[m.name] = m.srcPath
  z.modulesByName[m.name] = m
  ma.addAlias(m.name, m.srcPath)
}
for (let p of z.resolvePaths) {
  ma.addPath(p)
}

global.JC_NODEJS_MODULES = z

module.exports = z

""")

        return sb.toString()
    }

    /**
     * Генерить webpack-dummy.config.js
     */
    String makeWebpackDummy() {
        String template = """\
//
// THIS FILE GENERATED, NOT MODIFY!
//
let z = require('./${FILE_JC_NODEJS_MODULES}')

module.exports = {
    entry:  './${FILE_WEBPACK_DUMMY_ENTRY}',
    resolve: {
        modules: [
            ...z.resolvePaths
        ],
        alias: {
            ...z.modulesPaths
        }
    }
}
"""
        return template
    }

    Map sortDependsMap(Map deps) {
        Map res = new LinkedHashMap<>()
        if (deps == null) {
            return res
        }
        List<String> tmp = new ArrayList<>(deps.keySet())
        Collections.sort(tmp)
        for (String s : tmp) {
            res.put(s, UtCnv.toString(deps.get(s)))
        }
        return res
    }

    /**
     * Обновить package.json текущим составом библиотек
     */
    void updatePackageJson(String fn) {
        // не существует
        if (!UtFile.exists(fn)) {
            String s = UtJson.toJson(makeCommonPackageJson(), true)
            ant.echo(message: s, file: fn)
            return
        }

        // загружаем существующий
        Map m
        String s = UtFile.loadString(fn)
        try {
            m = (Map) UtJson.fromJson(s)
        } catch (e) {
            s = UtJson.toJson(makeCommonPackageJson(), true)
            ant.echo(message: s, file: fn)
            return
        }

        Map newDeps = grabAllDepends()
        Map oldDeps = sortDependsMap(m['dependencies'])

        if (oldDeps.toString() != newDeps.toString()) {
            String tmpFile = wd("temp/${UtDateTime.now().toString(UtDateTime.createFormatter("yyyy-MM-dd--HHmmss"))}--dependencies.json")
            log.warn "Состав dependencies в ${fn} изменился. Предыдущий вариант в ${tmpFile}"
            ant.echo(message: UtJson.toJson([dependencies: oldDeps], true), file: tmpFile)

            // новый вариант
            m['dependencies'] = newDeps
            s = UtJson.toJson(m, true)

            ant.echo(message: s, file: fn)
        }

    }

    /**
     * Обновить none_modules
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

    /**
     * Имя nodejs модуля в имя модуля для idea
     */
    String nodeJsModuleNameToIdeaModuleName(String s) {
        s = s.replace("\\", "/")
        if (!s.startsWith("@")) {
            s = "nodejs--" + s
        }
        s = s.replace("/", "--")
        return s
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

}
