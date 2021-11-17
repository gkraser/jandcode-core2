package jandcode.core.nodejs.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

class NodeJsGenIdea extends ProjectScript {

    protected void onInclude() throws Exception {
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)
        onEvent(GenIdea.Event_GenIws, this.&genIwsHandler)
    }

    ////// idea

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x

        SimXml x1

        NodeJsUtils nut = create(NodeJsUtils)
        def mods = nut.getAllModules()

        // для маппинга библиотек nodejs
        def x_libMap = x.root.findChild("component@name=JavaScriptLibraryMappings", true)
        x_libMap.clearChilds()

        for (mod in mods) {
            if (mod.ownerProject == null) {
                continue
            }
            String ideaModuleName = nut.nodeJsModuleNameToIdeaModuleName(mod.name)
            String ideaImlFile = UtFile.join(mod.path, ideaModuleName + ".iml")

            log.info("Generate iml: ${ideaImlFile}")

            ImlXml iml = getWebImlXml(ideaImlFile)
            iml.root.save().toFile(ideaImlFile)

            def x_mod = x.addModule(ideaImlFile)

            // группа
            if (!pathInProjectDir(mod.path)) {
                // группы генерим только для модулей вне каталога проекта
                // иначе они попадают и в группу и доступны внутри проекта,
                // что вносит сумятицу
                String grpName = "nodejs"
                def grpParts = mod.name.split("/")
                if (grpParts.length > 1) {
                    grpName = grpName + "/" + grpParts[0]
                }
                x_mod['group'] = grpName
            }

            // nodejs mapping
            SimXml xx2 = x_libMap.addChild("file")
            xx2['url'] = "file://" + mod.path
            xx2['libraries'] = "{HTML, Node.js Core}"

        }

        // поддержка конфига webpack
        x1 = x.root.findChild("component@name=WebPackConfiguration/option@name=mode", true)
        x1['value'] = "MANUAL"
        x1 = x.root.findChild("component@name=WebPackConfiguration/option@name=path", true)
        x1['value'] = nut.getMetaDataPath(nut.PATH_WEBPACK_DUMMY)

    }

    /**
     * Проверка, что путь указывает на путь внутри проекта
     * @param path какой путь проверяем
     * @return true, если находится внутри проекта
     */
    boolean pathInProjectDir(String path) {
        path = UtFile.abs(path)
        String pd = wd()
        if (path === pd) {
            return true
        }
        pd = pd + File.separator
        return path.startsWith(pd)
    }

    void genIwsHandler(GenIdea.Event_GenIws e) {
        IwsXml x = e.x

        NodeJsUtils nut = create(NodeJsUtils)

        // поддержка Node Js
        String nodeExe = nut.getNodeExe()
        if (nodeExe) {
            String nodeVer = nut.getNodeVersion(nodeExe)
            if (nodeVer) {
                SimXml x1 = x.root.findChild("component@name=PropertiesComponent", true)
                x1['property@name=nodejs_interpreter_path:value'] = nodeExe.replace('\\', '/')
                x1['property@name=javascript.nodejs.core.library.configured.version:value'] = nodeVer
            }
        }


        def fillEnvs = { SimXml envs ->
            //sysenv =
            String s

            s = System.getenv("PATH")
            envs["env@name=PATH:value"] = s

            s = System.getenv("NODE_PATH")
            if (!UtString.empty(s)) {
                envs["env@name=NODE_PATH:value"] = s
            }

            s = System.getenv("NODE_OPTIONS")
            if (!UtString.empty(s)) {
                envs["env@name=NODE_OPTIONS:value"] = s
            }
        }
        // запуск js через node по умолчанию
        SimXml envs

        SimXml x1 = x.addDefaultRunConfig("NodeJSConfigurationType", "Node.js")
        envs = x1.findChild("envs", true)
        fillEnvs(envs)

        // запуск gulp
        x1 = x.addDefaultRunConfig("js.build_tools.gulp", "Gulp.js")
        envs = x1.findChild("envs", true)
        fillEnvs(envs)

        // запуск npm
        x1 = x.addDefaultRunConfig("js.build_tools.npm", "")
        x1.findChild('package-json', true)['value'] = "\$PROJECT_DIR\$/package.json"
        x1.findChild('command', true)['value'] = "run"
        envs = x1.findChild("envs", true)
        fillEnvs(envs)

        // mocha
        String mochaPak = UtFile.join(nut.getMetaDataPath(nut.PATH_NODE_MODULES), "mocha")
        if (UtFile.exists(mochaPak)) {
            x1 = x.addDefaultRunConfig("mocha-javascript-test-runner", "")
            x1.findChild("mocha-package", true).setText(mochaPak)
            envs = x1.findChild("envs", true)
            fillEnvs(envs)
        }

    }

    /**
     * Получиение iml-файла для модуля
     */
    ImlXml getWebImlXml(String path) {
        SimXml x = new SimXmlNode()

        // если есть файл модуля, грузим его, иначе - шаблон
        if (UtFile.exists(path)) {
            x.load().fromFile(path)
        } else {
            String tf = ctx.service(JcDataService).getFile("idea/iml-template-web.xml")
            x.load().fromFile(tf)
        }
        ImlXml ix = new ImlXml(project, x)

        return ix
    }

}
