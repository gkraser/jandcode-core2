package jandcode.jc.std

import jandcode.commons.error.*
import jandcode.jc.*
import jandcode.jc.std.junit.*

/**
 * Поддержка unit-тестирования с помощью junit5
 */
class JUnitProject extends ProjectScript {

    /**
     * Отчет о unit-тестирование в xml
     */
    String dirTestreportXml = "temp/testreport.xml"

    /**
     * Отчет о unit-тестирование в html
     */
    String dirTestreportHtml = "temp/testreport.html"

    /**
     * Признак выполнения тестов с ошибками. Устанавливается при выполнении команды test.
     * Используется при груповом выполнении тестов в проектах типа root.
     */
    boolean testExecFail = false


    protected void onInclude() throws Exception {

        afterLoad(this.&onAfterLoad)

        cm.add("test", this.&cmTest, "Запуск unit-тестов",
                cm.opt("q", false, "Не генерировать ошибки при проваленных тестах"),
                cm.opt("c", "", "Имя класса с тестами, запускается только он, иначе - все"),
                cm.opt("m", "", "Для опции: -c. Имена тестовых методов через ','. По умолчанию - все"),
        )

    }

    JavaProject getJavaProject() {
        JavaProject p = getIncluded(JavaProject)
        if (p == null) {
            throw new XError("Скрипт ${getClass().getName()} должен работать совместно с ${JavaProject.class.getName()}")
        }
        return p
    }

    void onAfterLoad() {
        javaProject.depends.dev.add(
                'junit-jupiter-api',
                'junit-jupiter-params',
                'junit-jupiter-engine',
                'junit-platform-launcher',
                'apiguardian-api',
        )
    }

    void cmTest(CmArgs args) {
        JavaProject jm = getJavaProject()

        // если имеется команда prepare, выполняем ее перед тестами
        if (cm.find("prepare") != null) {  //todo это надо? Это надо тут?
            cm.exec("prepare")
        }
        //
        jm.compileTest()
        runTest(!args.q, !args.z, args.c, args.m)
    }

    /**
     * Запуск unit-тестов
     */
    void runTest(boolean trowError, boolean forkJvm, String testClassName, String testMethods) {
        JavaProject jm = getJavaProject()

        testExecFail = false
        if (jm.dirsSrcTest.size() == 0) {
            log.info "Test not defined for: ${project.name}"
            return
        }
        log.info("Run tests: ${project.name}")

        ut.cleandir(wd(dirTestreportXml))
        ut.cleandir(wd(dirTestreportHtml))

        // запускаем
        classpath("jandcode-jc-junit")
        IJUnitRunner runner = include("jandcode.jc.junit.JUnitRunner")
        testExecFail = runner.runTests(testClassName, testMethods)

        log.info "Report tests: ${project.name}"
        classpath("ant-junit")
        ant.junitreport(todir: wd(dirTestreportHtml)) {
            fileset(dir: wd(dirTestreportXml)) {
                include(name: "**/*.xml")
            }
            report(format: "frames", todir: wd(dirTestreportHtml))
        }

        // ошибки при выполнении тестов
        if (testExecFail) {
            // если нужно, генерим ошибку
            if (trowError) {
                error("Run tests error in project [${project.name}]")
            } else {
                log.warn("Run tests error in project [${project.name}]")
            }
        }
    }

}
