package jandcode.mdoc.jc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.commons.variant.*
import jandcode.core.*
import jandcode.core.web.undertow.*
import jandcode.jc.*
import jandcode.jc.impl.depends.*
import jandcode.jc.std.*
import jandcode.mdoc.*
import jandcode.mdoc.builder.*
import jandcode.mdoc.cfg.*
import jandcode.mdoc.web.*

class MDocProject extends ProjectScript implements ILibDepends {

    /**
     * Конфигурация документации
     */
    DocCfg docCfg = new DocCfg()

    /**
     * Куда генерим
     */
    String outDir = "temp/mdoc-doc"

    /**
     * Дополнительные модули, которые необходимы для запуска mdoc.
     * Например расширения, темы.
     * Используется только prod, остальные группы игнорируются
     */
    LibDepends depends

    /**
     * Запускалка редактора
     */
    class CmdRunnerImpl implements CmdRunner {

        private String editorCmd = "mdoc-editor"

        void runEditor(String file, int lineNumber) throws Exception {
            String editor = ut.findcmd(cmd: editorCmd)
            if (UtString.empty(editor)) {
                //todo linux!
                editor = ctx.service(JcDataService).findFile("mdoc/${editorCmd}.bat")
            }
            ut.runcmd(cmd: "${editor} ${file} ${lineNumber}")
        }

    }

    protected void onInclude() throws Exception {

        this.depends = new LibDependsImpl(ctx, this)
        this.depends.prod.add(
                "jandcode.mdoc",
                "jandcode.mdoc.web",
        )

        // генерация исходников
        include(MDocGen)

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

    ////// config utilites

    /**
     * Свойства документа
     */
    IVariantMap getProps() {
        return docCfg.getProps()
    }

    /**
     * Добавить каталог с исходниками 
     * @param dir каталог
     * @param params параметры: prefix, resources, includes, excludes
     * @return
     */
    FilesetCfg addSrc(Map params, String dir) {
        FilesetCfg fs = docCfg.addSrc(wd(dir))
        if (params != null) {
            fs.setProps(params)
        }
        return fs
    }

    /**
     * Добавить каталог с исходниками
     */
    FilesetCfg addSrc(String dir) {
        return addSrc(null, dir)
    }

    /**
     * см {@link jandcode.mdoc.jc.MDocGen#genSrc(java.lang.String, java.lang.String, groovy.lang.Closure)}
     */
    void genSrc(String name, String destdir, Closure doTask) {
        include(MDocGen).genSrc(name, destdir, doTask)
    }

    /**
     * см {@link jandcode.mdoc.jc.MDocGen#genSrc(java.lang.String, java.lang.String, groovy.lang.Closure)}
     */
    void genSrc(String destdir, Closure doTask) {
        include(MDocGen).genSrc("", destdir, doTask)
    }

    //////

    App loadApp() {

        // формируем app.cfx
        String appConfTemp = wd("_jc/generated-mdoc-app.cfx")
        def x = new SimXmlNode()

        def x1

        x.setValue("app:appdir", wd())

        for (String m : depends.prod.modules) {
            x1 = x.addChild("x-depends")
            x1.setValue("module", m)
        }

        ut.cleanfile(appConfTemp)
        x.save().toFile(appConfTemp)

        // включаем все нужные модули в classpath
        classpath(depends.prod.names)

        // грузим
        App app = AppLoader.load(appConfTemp)

        // настраиваем
        app.beanFactory.registerBean(CmdRunner.class.name, new CmdRunnerImpl())

        return app
    }

    Doc loadDoc(App app) {
        MDocService svc = app.bean(MDocService)
        ut.stopwatch.start("load doc")
        Doc doc = svc.createDocument(docCfg)
        doc.load()
        ut.stopwatch.stop("load doc")
        return doc
    }

    void cmBuild(CmArgs args) {
        // генерируем исходники
        if (!args.nogen) {
            include(MDocGen).cmGenSrc()
        }

        App app = loadApp()
        Doc doc = loadDoc(app)
        OutBuilder builder = doc.createBuilder("html")

        ut.stopwatch.start("build doc")
        builder.build()
        ut.stopwatch.stop("build doc")

        ut.stopwatch.start("out doc")
        String outDir = wd(this.getOutDir())
        ut.cleandir(outDir)
        builder.outTo(new OutDir(outDir))
        ant.echo(message: project.version, file: "${outDir}/VERSION")
        ut.stopwatch.stop("out doc")
    }

    void cmServe(CmArgs args) {
        // генерируем исходники
        if (!args.nogen) {
            include(MDocGen).cmGenSrc()
        }

        App app = loadApp()
        Doc doc = loadDoc(app)
        OutBuilder builder = doc.createBuilder("html")

        // регистрируем то,что будем показывать
        app.bean(WebMDocService).registerBuilder("default", builder)

        // режимы
        builder.mode.serve = true
        builder.mode.debug = true

        ut.stopwatch.start("build doc")
        builder.build()
        ut.stopwatch.stop("build doc")

        // запускаем сервер
        UndertowRunner r = new UndertowRunner();
        r.port = args.getInt("p", 4000)
        r.context = "/"
        r.start(app);

        println(ut.makeDelim("mdoc serve"))
        println(r.getUrl())
        println(ut.makeDelim(""))

        // останавливаемся тут, иначе jc доходит до остановки и сбрасывает ansi
        while (true) {
            Thread.sleep(10000000)
        }
    }

    //////

    void buildHandler() {
        cm.exec("mdoc-build")
    }

}
