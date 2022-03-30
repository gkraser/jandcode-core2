package jandcode.core.jc

import jandcode.commons.*
import jandcode.commons.conf.*
import jandcode.commons.variant.*
import jandcode.core.*
import jandcode.jc.*
import jandcode.jc.impl.*
import jandcode.jc.std.*

import java.text.*

/**
 * Поддержка App для проекта
 */
class AppProject extends ProjectScript implements ILibDependsGrab {

    /**
     * Событие: запись appConf
     */
    public static class Event_SaveAppConf extends BaseJcEvent {
        /**
         * каталог, в который выводится app.conf
         */
        String outDir

        Event_SaveAppConf(String outDir) {
            this.outDir = outDir
        }
    }

    /**
     * Событие: показать appinfo
     */
    public static class Event_ShowInfo extends BaseJcEvent {
        /**
         * Приложение, для которого нужно показать информацию
         */
        App app

        Event_ShowInfo(App app) {
            this.app = app
        }
    }


    private App _app
    private boolean _prepareSourceOk

    /**
     * Ссылка на приложение.
     * Приложение грузится из файла {@link AppConsts#FILE_APP_CONF},
     * который находится в корне проекта.
     */
    App getApp() {
        if (_app == null) {
            String mf = wd(AppConsts.FILE_APP_CONF)
            log.info("load app from [${mf}]")
            // depends
            LibDepends deps = create(LibDependsUtils).getDepends(project)
            ListLib libs = deps.all.libsAll
            if (log.verbose) {
                log.debug(ut.makePrintClasspath(libs))
            }
            ctx.classpath(libs)

            // перезагрузить registry-module-def.cfx, если необходимо
            prepareSource()

            ut.stopwatch.start("load app")
            //
            _app = AppLoader.load(mf)
            //
            ut.stopwatch.stop("load app")
        }
        return _app
    }

    protected void onInclude() throws Exception {
        cm.add("app-showinfo", this.&cmShowinfo, "Информация о приложении")
        cm.add("app-saveappconf", this.&cmSaveAppConf, "Записать App.getConf() в файл")
    }

    void cmShowinfo() {
        App a = getApp() //load!
        Map m

        //
        println ut.makeDelim("app")
        m = [:]
        m['appConfFile'] = a.appConfFile
        m['appdir'] = a.appdir
        m['workdir'] = a.workdir
        m['env.dev'] = a.env.dev
        ut.printMap(m)

        //
        println ut.makeDelim("modules")
        m = [:]
        for (ModuleInst module : a.modules) {
            m[module.name] = module.path
        }
        ut.printMap(m)

        //
        println ut.makeDelim("beans")
        m = [:]
        for (BeanDef b : a.getBeanFactory().getBeans()) {
            m[b.name] = b.getCls().getName()
        }
        ut.printMap(m)

        // генерим событие для тех, кто еще что то показать желает
        fireEvent(new Event_ShowInfo(a))
    }

    void cmSaveAppConf(IVariantNamed args) {
        Conf conf = app.conf  //load!
        //
        String outDir = wd("temp/appconf")
        ut.cleandir(outDir)
        String fn = "${outDir}/app-{0}.cfx"
        //
        def f = MessageFormat.format(fn, "ALL")
        log "save file [${f}]"
        UtConf.save(conf).toFile(f)

        for (m in app.modules) {
            f = "${outDir}/module--${m.name}.cfx"
            log "save file [${f}]"
            UtConf.save(m.conf).toFile(f)
        }

        // генерим событие для тех, кто еще что то записать желает
        fireEvent(new Event_SaveAppConf(outDir))
    }

    /**
     * Подготовить приложение к загрузке из исходников.
     * Выполняется, если среди всех библиотек имеются библиотеки в исходниках.
     */
    void prepareSource() {
        if (_prepareSourceOk) {
            return
        }
        // перезагрузить registry-module-def.cfx, если необходимо
        new ModuleDefProjectUtils(this).updateRegistryModuleDef()
        //
        _prepareSourceOk = true
    }

    void grabDepends(LibDepends deps) {
        JavaProject jp = getIncluded(JavaProject)
        if (jp != null) {
            // AppProject подключен к JavaProject, значит он тоже зависимость!
            deps.dev.add(project.name)
        }
    }

}
