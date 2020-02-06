package jandcode.core.jsa.jc

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

class JsaRootProject extends ProjectScript {

    /**
     * Полное имя jar-файла со скомпиленными клиенскими файлами.
     */
    String getFileJsaWebrootJar() {
        return wd("temp/lib/jsa-webroot.jar")
    }

    protected void onInclude() throws Exception {

        include(JsaShowlibs)

        cm.add("jsa-build", "Выполнить сборку клиенского кода (gulp build)", this.&cmJsaBuild)
        cm.add("jsa-watch", "Собрать и следить за изменениями клиенского кода (gulp watch)", this.&cmJsaWatch)

        // для RootProject
        afterLoad {
            RootProject rp = getIncluded(RootProject)
            if (rp == null) {
                throw new XError("${RootProject.class.name} должен быть включен в проект для ${this.class.name}")
            }

            // prepare
            include(PrepareProject)
            onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

            // idea
            include(GenIdea)
            onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)
            onEvent(GenIdea.Event_GenIws, this.&genIwsHandler)

            // build
            include(BuildProject)
            onEvent(BuildProject.Event_Build, this.&buildHandler)

        }
    }

    void prepareHandler() {
        JsaUtils jsaUtils = create(JsaUtils)

        log "jsa prepare for: ${name}"
        ant.mkdir(dir: wd(JcConsts.JC_METADATA_DIR))

        // jc-env.bat
        String s = jsaUtils.makeJcEnvBat()
        StringBuilder jcEnvText = include(JcEnvBuilder).text
        jcEnvText.append(s)

        // jsa-modules.js
        s = jsaUtils.makeJsaModulesJs()
        UtFile.saveString(s, new File(wd(JcConsts.JC_METADATA_DIR + "/" + JsaConsts.JSA_MODULES)))

        // webpack dummy
        s = jsaUtils.makeJsaWebpackDummy()
        UtFile.saveString(s, new File(wd(JcConsts.JC_METADATA_DIR + "/" + JsaConsts.WEBPACK_DUMMY_CONFIG)))
        UtFile.saveString("//", new File(wd(JcConsts.JC_METADATA_DIR + "/" + JsaConsts.WEBPACK_DUMMY_ENTRY)))

        // prepare-data
        List<String> pdd = ctx.service(JcDataService).vdir.getRealPathList("jsa/prepare-data")
        String pdd_dest = wd(JcConsts.JC_METADATA_DIR + "/jsa-prepare-data")
        ant.mkdir(dir: pdd_dest)
        for (String pdd1 : pdd) {
            ant.copy(todir: pdd_dest) {
                fileset(dir: pdd1)
            }
        }

        // package.json
        jsaUtils.updatePackageJson(wd(JsaConsts.PACKAGE_JSON))

        // node_modules
        jsaUtils.updateNodeModules(wd(JsaConsts.PACKAGE_JSON))

        // gulpfile
        String gulpfile = wd("gulpfile.js")
        if (!UtFile.exists(gulpfile)) {
            log.warn("Файл ${gulpfile} не существует и будет создан. Не забудьте его закомитить")
            String srcGulpfile = ctx.service(JcDataService).getFile("jsa/gulp/gulpfile-default.js")
            ant.copy(file: srcGulpfile, tofile: gulpfile)
        }
    }

    void cmJsaBuild() {
        cm.exec("prepare")

        String env = ""
        if (ctx.env.prod) {
            env = "cross-env \"NODE_ENV=production\""
        }
        ut.runcmd(cmd: "jc @ ${env} gulp build")

        if (ctx.env.prod) {
            log.info("build jar: ${getFileJsaWebrootJar()}")
            // собираем jar
            ant.jar(destfile: getFileJsaWebrootJar()) {
                zipfileset(dir: "_gen/jsa-webroot", prefix: "jsa-webroot") {
                    include(name: '**/*')
                    exclude(name: '**/_tst/**/*')
                    exclude(name: '**/_tst')
                }
            }
        }
    }

    void cmJsaWatch() {
        cm.exec("prepare")

        String env = ""
        if (ctx.env.prod) {
            env = "cross-env \"NODE_ENV=production\""
        }
        ut.runcmd(cmd: "jc @ ${env} gulp watch")
    }

    ////// idea

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x

        SimXml x1

        JsaUtils jsaUtils = create(JsaUtils)
        List<JsaModule> moduleInfos = jsaUtils.getJsaModules()

        // поддержка Node Js
        x1 = x.root.findChild("component@name=JavaScriptLibraryMappings", true)
        for (JsaModule mi : moduleInfos) {
            Project p = mi.lib.sourceProject
            if (p == null) {
                continue
            }

            //
            def findFileChild = { SimXml xx, String url ->
                for (SimXml xx1 : xx) {
                    if (xx1.hasName('file') && xx1['url'] == url) {
                        return xx1
                    }
                }
                SimXml xx2 = xx.addChild("file")
                xx2['url'] = url
                return xx2
            }
            //

            String relPath = UtFile.getRelativePath(wd(""), p.wd()).replace('\\', '/')
            String url = "file://\$PROJECT_DIR\$/${relPath}"
            SimXml x2 = findFileChild(x1, url)
            String libs = x2.getString('libraries')
            libs = libs.replace('{', '').replace('}', '')
            List<String> libsList = UtCnv.toList(libs, ',')
            String nodeJsLib = 'Node.js Core'
            if (!libsList.contains(nodeJsLib)) {
                libsList.add(nodeJsLib)
                libs = '{' + UtString.join(libsList, ', ') + '}'
                x2['libraries'] = libs
            }
        }

        // поддержка конфига webpack
        x1 = x.root.findChild("component@name=WebPackConfiguration/option@name=path", true)
        x1['value'] = '$PROJECT_DIR$/' + JcConsts.JC_METADATA_DIR + '/' + JsaConsts.WEBPACK_DUMMY_CONFIG
    }

    void genIwsHandler(GenIdea.Event_GenIws e) {
        IwsXml x = e.x

        JsaUtils jsaUtils = create(JsaUtils)

        // поддержка Node Js
        String nodeExe = jsaUtils.getNodeExe()
        if (nodeExe) {
            String nodeVer = jsaUtils.getNodeVersion(nodeExe)
            if (nodeVer) {
                SimXml x1 = x.root.findChild("component@name=PropertiesComponent", true)
                x1['property@name=nodejs_interpreter_path:value'] = nodeExe.replace('\\', '/')
                x1['property@name=javascript.nodejs.core.library.configured.version:value'] = nodeVer
            }
        }

        // запуск js через node по умолчанию
        SimXml x1 = x.addDefaultRunConfig("NodeJSConfigurationType", "Node.js")
        x1['node-parameters'] = "-r ${wd(JcConsts.JC_METADATA_DIR + "/" + JsaConsts.JSA_MODULES)}"

        // запуск gulp
        x1 = x.addDefaultRunConfig("js.build_tools.gulp", "Gulp.js")
        x1.findChild('node-options', true).text = "-r ${wd(JcConsts.JC_METADATA_DIR + "/" + JsaConsts.JSA_MODULES)}"

    }

    //////

    void buildHandler() {
        cm.exec("jsa-build")
    }


}
