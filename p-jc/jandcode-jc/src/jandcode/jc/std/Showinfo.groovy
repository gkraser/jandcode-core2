package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Команда и утилиты для показа основных свойств проекта
 */
class Showinfo extends ProjectScript {

    /**
     * Событие: получение дополнительной информации о проекте
     */
    public static class Event_GetInfo extends BaseJcEvent {
        /**
         * map с информацией, которая будет напечатана
         */
        Map m

        Event_GetInfo(Map m) {
            this.m = m
        }

    }


    protected void onInclude() throws Exception {
        cm.add("showinfo", this.&cmShowinfo, "Информация о проекте",
                cm.opt("a", false, "Показать все")
        )
    }

    void cmShowinfo(CmArgs args) {
        def m = [:]
        m['Name'] = project.name
        m['Version'] = project.version
        m['Project file'] = project.projectFile
        m['Project home'] = project.wd.path
        m['Java version'] = System.getProperty("java.version")
        m['Java home'] = System.getProperty("java.home")

        List autoLoad = ctx.config.autoLoadProjects
        if (autoLoad.size() > 0) {
            m['Auto load'] = autoLoad
        }

        JavaVars jv = include(JavaVars)
        String s
        s = jv.targetLevel
        if (!UtString.empty(s)) {
            m['Java targetLevel'] = s
        }
        s = jv.sourceLevel
        if (!UtString.empty(s)) {
            m['Java sourceLevel'] = s
        }

        if (args.a) {

            m['Included'] = impl(Object).collect {
                ctx.getFileProjectScript(it.class) ?: it.class.name
            }

            m["${JcConsts.JC_DATA_DIR} paths"] = ctx.service(JcDataService).vdir.getRoots().collect {
                it.rootPath
            }

            m['Loaded projects'] = ctx.projects.collect {
                it.wd("")
            }

            // собираем еще
            fireEvent(new Event_GetInfo(m))
        }
        //
        ut.printMap(m)
    }
}
