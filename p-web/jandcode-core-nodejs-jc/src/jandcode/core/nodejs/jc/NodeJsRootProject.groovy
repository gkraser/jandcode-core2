package jandcode.core.nodejs.jc

import jandcode.commons.*
import jandcode.core.jc.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Корневой проект для nodejs
 */
class NodeJsRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        // является и просто проектом nodejs
        include(NodeJsProject)
        // является и средой для nodejs
        include(NodeJsEnv)

        // build
        include(BuildProject)
        onEvent(BuildProject.Event_Build, this.&buildHandler)

        // product
        onEvent(AppProductBuilder.Event_Exec, this.&productHandler)

        cm.add("nodejs-build", "Выполнить сборку клиенского кода (npm run build)", this.&cmNodejsBuild,
                cm.opt('prod', 'Запустить в режиме production')
        )
        cm.add("nodejs-watch", "Собрать и следить за изменениями клиенского кода (npm run watch)", this.&cmNodejsWatch,
                cm.opt('prod', 'Запустить в режиме production')
        )

    }

    /**
     * Корневой каталог ресурсов для скомпилированных файлов nodejs.
     * По соглашению с модулем jandcode-core-web
     */
    String resourceRoot = "resource-webroot"

    /**
     * Корневой каталог внутри mainModule, куда осуществляется генерация файлов.
     * Этот каталог будет доступен в приложении как '/'
     */
    String webrootDir = "_gen"


    private String _mainModule

    /**
     * Модули nodejs, которые содержатся в проекте.
     * Параметром может быть каталог, маска.
     * Вызов транслируется в NodeJsProject
     */
    void modules(String... modules) {
        include(NodeJsProject).modules(modules)
    }

    /**
     * Каталог с главным js-модулем приложения.
     */
    String getMainModule() {
        def res = this._mainModule
        if (UtString.empty(res)) {
            return wd("")
        }
        return wd(res)
    }

    void setMainModule(String v) {
        this._mainModule = v
    }

    //////

    /**
     * Полное имя jar-файла со скомпиленными клиенскими файлами.
     */
    String getFileNodejsWebrootJar() {
        return wd("temp/lib/nodejs-${resourceRoot}.jar")
    }

    void cmNodejsWatch(CmArgs args) {
        boolean isProd = args.containsKey('prod')

        cm.exec("prepare")

        String env = ""
        if (ctx.env.prod || isProd) {
            env = "cross-env \"NODE_ENV=production\""
        }
        log "npm run watch in ${mainModule}"
        ut.runcmd(cmd: "jc @ ${env} npm run watch", dir: mainModule)
    }

    void cmNodejsBuild(CmArgs args) {
        boolean isProd = args.containsKey('prod')
        cm.exec("prepare")

        String env = ""
        if (ctx.env.prod || isProd) {
            env = "cross-env \"NODE_ENV=production\""
        }
        log "npm run build in ${mainModule}"
        ut.runcmd(cmd: "jc @ ${env} npm run build", dir: mainModule)

        if (ctx.env.prod) {
            log.info("build jar: ${getFileNodejsWebrootJar()}")

            // откуда забираем скомпиленное
            String srcPath = wd.dir(mainModule).join(webrootDir)

            // собираем jar
            ant.jar(destfile: getFileNodejsWebrootJar()) {
                zipfileset(dir: srcPath, prefix: "${resourceRoot}") {
                    include(name: '**/*')
                }
            }
        }
    }

    void buildHandler() {
        cm.exec("nodejs-build")
    }

    void productHandler(AppProductBuilder.Event_Exec e) {
        AppProductBuilder builder = e.builder

        // nodejs-resouurce-webroot.jar
        ant.copy(file: getFileNodejsWebrootJar(), todir: "${builder.destDir}/lib")
    }

}
