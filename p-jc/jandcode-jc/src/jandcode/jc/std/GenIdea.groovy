package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.idea.*

/**
 * Утилиты для генерация файлов idea.
 * Скрипт включается в проект и для проекта, в который он включен можно вызывать методы
 * genIpr(), genIml(), genIws().
 * Проект в свою очередь должен предоставлять события GenIdea.Event_xxx для модификации
 * генерируемых файлов.
 */
class GenIdea extends ProjectScript {

    /**
     * Событие: генерация iml
     */
    public static class Event_GenIml extends BaseJcEvent {
        /**
         * Генерируемый файл
         */
        ImlXml x

        Event_GenIml(ImlXml x) {
            this.x = x
        }
    }

    /**
     * Событие: генерация ipr
     */
    public static class Event_GenIpr extends BaseJcEvent {
        /**
         * Генерируемый файл
         */
        IprXml x

        Event_GenIpr(IprXml x) {
            this.x = x
        }
    }

    /**
     * Событие: генерация iws
     */
    public static class Event_GenIws extends BaseJcEvent {
        /**
         * Генерируемый файл
         */
        IwsXml x

        Event_GenIws(IwsXml x) {
            this.x = x
        }
    }

    /**
     * Имя генерируемого проекта idea. Если не установлено, используется имя проекта
     */
    String ideaProjectName

    String getIdeaProjectName() {
        if (UtString.empty(ideaProjectName)) {
            return project.name
        }
        return ideaProjectName
    }

    //////

    /**
     * Генерация файла iws
     */
    void genIws() {
        def outFile = project.wd.join("${getIdeaProjectName()}.iws")
        SimXml x = new SimXmlNode()

        log.info("Generate iws: ${outFile}")

        // если есть файл модуля, грузим его, иначе - шаблон
        if (UtFile.exists(outFile)) {
            x.load().fromFile(outFile)
        } else {
            String tf = ctx.service(JcDataService).getFile("idea/iws-template.xml")
            ctx.debug("idea template: " + tf)
            x.load().fromFile(tf)
        }
        IwsXml ix = new IwsXml(project, x)

        // уведомляем
        fireEvent(new Event_GenIws(ix))

        // сохраняем
        x.save().toFile(outFile)
    }

    /**
     * Генерация файла iml
     */
    void genIml() {
        def outFile = project.wd.join("${getIdeaProjectName()}.iml")
        SimXml x = new SimXmlNode()

        log.info("Generate iml: ${outFile}")

        // если есть файл модуля, грузим его, иначе - шаблон
        if (UtFile.exists(outFile)) {
            x.load().fromFile(outFile)
        } else {
            String tf = ctx.service(JcDataService).getFile("idea/iml-template.xml")
            ctx.debug("idea template: " + tf)
            x.load().fromFile(tf)
        }
        ImlXml ix = new ImlXml(project, x)

        // уведомляем
        fireEvent(new Event_GenIml(ix))

        // сохраняем
        x.save().toFile(outFile)
    }

    /**
     * Генерация файла ipr
     */
    void genIpr() {
        def outFile = project.wd.join("${getIdeaProjectName()}.ipr")
        SimXml x = new SimXmlNode()

        log.info("Generate ipr: ${outFile}")

        // если есть файл модуля, грузим его, иначе - шаблон
        if (UtFile.exists(outFile)) {
            x.load().fromFile(outFile)
        } else {
            String tf = ctx.service(JcDataService).getFile("idea/ipr-template.xml")
            ctx.debug("idea template: " + tf)
            x.load().fromFile(tf)
        }
        IprXml ix = new IprXml(project, x)

        // ставим значения по умолчанию
        assignIprDefaultValues(ix)

        // уведомляем
        fireEvent(new Event_GenIpr(ix))

        // сохраняем
        x.save().toFile(outFile)
    }

    void assignIprDefaultValues(IprXml x) {

        String s
        SimXml x1

        // jdk
        JdkInfo jdkInfo = new JdkInfo()
        s = jdkInfo.getJdkVersion()
        x1 = x.root.findChild("component@name=ProjectRootManager", true)
        if (x1.getString('project-jdk-name') == '') {
            x1['project-jdk-name'] = s
        }

        // lang level
        s = jdkInfo.getIdeaLanguageLevelForJdkVersion(jdkInfo.getJdkVersion())
        x1 = x.root.findChild("component@name=ProjectRootManager", true)
        if (x1.getString('languageLevel') == '') {
            x1['languageLevel'] = s
        }

    }

}
