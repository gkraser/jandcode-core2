package jandcode.core.nodejs.jc

import jandcode.commons.*
import jandcode.core.web.jc.*
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

        cm.add("nodejs-build", "Выполнить сборку клиенского кода (npm run build)", this.&cmNodejsBuild,
                cm.opt('prod', 'Запустить в режиме production')
        )
        cm.add("nodejs-watch", "Собрать и следить за изменениями клиенского кода (npm run watch)", this.&cmNodejsWatch,
                cm.opt('prod', 'Запустить в режиме production')
        )

        //
        afterLoad {
            // откуда забираем скомпиленное
            String srcPath = wd.dir(mainModule).join(webrootDir)
            // включаем его в jar для продукта
            include(WebRootProject).addResourceWebroot(srcPath)
        }
    }

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
    }

    void buildHandler() {
        cm.exec("nodejs-build")
    }

}
