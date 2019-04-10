package jandcode.mdoc.jc

import jandcode.commons.error.*
import jandcode.core.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.mdoc.*
import jandcode.mdoc.builder.*
import jandcode.mdoc.web.*
import jandcode.undertow.*
import jandcode.web.*
import jandcode.web.webxml.*

@Deprecated //todo пересмотреть. Тем более можно делать include для toc.xml
class MDocRootProject extends ProjectScript {

    /**
     * Список проектов
     */
    List<Project> projects = []

    protected void onInclude() throws Exception {

        // build
        include(BuildProject)
        onEvent(BuildProject.Event_Build, this.&buildHandler)


        cm.add("mdoc-build", "Генерация документации", this.&cmBuild,
                cm.opt("nogen", false, "Пропустить процесс генерации исходников"),
        )

        cm.add("mdoc-serve", "Запустить web-сервер для документации", this.&cmServe,
                cm.opt("nogen", false, "Пропустить процесс генерации исходников"),
                cm.opt("p", 4000, "Порт (по умолчанию 4000)"),
        )

    }

    void cmServe(CmArgs args) {

        WebXml x = new WebXml();
        WebXmlBuilder b = new WebXmlBuilder(x);

        App rootApp

        for (p in projects) {
            log "project: ${p.name}"

            def md = p.getIncluded(MDocProject)
            if (md == null) {
                throw new XError("Нет MDocProject в ${p.projectFile}")
            }

            if (!args.nogen) {
                p.include(MDocGen).cmGenSrc()
            }

            // корневым будет
            if (rootApp == null) {
                rootApp = md.loadApp()
            }

            App app = md.loadApp()
            Doc doc = md.loadDoc(app)
            OutBuilder builder = doc.createBuilder("html")
            // регистрируем то,что будем показывать
            app.bean(WebMDocService).registerBuilder("default", builder)

            rootApp.bean(WebMDocService).registerApp(p.name)

            // режимы
            builder.mode.serve = true
            builder.mode.debug = true

            ut.stopwatch.start("build doc")
            builder.build()
            ut.stopwatch.stop("build doc")

            //
            WebXml.Servlet sv = b.addServlet(p.name, "jandcode.web.AppServlet", "/" + p.name + "/*", 0);
            AppInstanceServlet svInst = new AppInstanceServlet();
            svInst.setApp(app);
            sv.setServletInstance(svInst)

        }

        //
        WebXml.Servlet sv = b.addServlet("root", "jandcode.web.AppServlet", "/*", 1);
        AppInstanceServlet svInst = new AppInstanceServlet();
        svInst.setApp(rootApp);
        sv.setServletInstance(svInst)

        // запускаем сервер
        UndertowRunner r = new UndertowRunner();
        r.port = args.getInt("p", 4000)
        r.context = "/"
        r.startWebXml(x);

        println(ut.makeDelim("mdoc serve"))
        println(r.getUrl())
        println(ut.makeDelim(""))

        // останавливаемся тут, иначе jc доходит до остановки и сбрасывает ansi
        while (true) {
            Thread.sleep(10000000)
        }

    }

    void cmBuild(CmArgs args) {

        for (p in projects) {
            log "project: ${p.name}"

            def md = p.getIncluded(MDocProject)
            if (md == null) {
                throw new XError("Нет MDocProject в ${p.projectFile}")
            }

            md.cm.exec("mdoc-build", args)
        }

    }

    //////

    void buildHandler() {
        cm.exec("mdoc-build")
    }

}
