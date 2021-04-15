package jandcode.core.nodejs.jc

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
     * Модули nodejs, которые содержатся в проекте.
     * Параметром может быть каталог, маска.
     * Вызов транслируется в NodeJsProject
     */
    void modules(String... modules) {
        include(NodeJsProject).modules(modules)
    }

    /**
     * Полное имя jar-файла со скомпиленными клиенскими файлами.
     */
    String getFileNodejsWebrootJar() {
        return wd("temp/lib/nodejs-webroot.jar")
    }

    void cmNodejsWatch(CmArgs args) {
        boolean isProd = args.containsKey('prod')

        cm.exec("prepare")

        String env = ""
        if (ctx.env.prod || isProd) {
            env = "cross-env \"NODE_ENV=production\""
        }
        ut.runcmd(cmd: "jc @ ${env} npm run watch")
    }

    void cmNodejsBuild(CmArgs args) {
        boolean isProd = args.containsKey('prod')
        cm.exec("prepare")

        String env = ""
        if (ctx.env.prod || isProd) {
            env = "cross-env \"NODE_ENV=production\""
        }
        ut.runcmd(cmd: "jc @ ${env} npm run build")

        if (ctx.env.prod) {
            log.info("build jar: ${getFileNodejsWebrootJar()}")
            // собираем jar
            ant.jar(destfile: getFileNodejsWebrootJar()) {
                zipfileset(dir: "_gen/nodejs-webroot", prefix: "nodejs-webroot") {
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

        // nodejs-webroot.jar
        ant.copy(file: getFileNodejsWebrootJar(), todir: "${builder.destDir}/lib")
    }

}
