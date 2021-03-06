package jandcode.jc

import groovy.transform.*
import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.test.*
import jandcode.jc.impl.*
import org.junit.jupiter.api.*

@CompileStatic
abstract class CustomProjectTestCase extends Utils_Test {

    Ctx ctx

    public static class DummyProjectScript extends ProjectScript {
        protected void onInclude() throws Exception {
        }
    }

    String getJcAppdir() {
        String s = System.getProperty(JcConsts.PROP_APP_DIR);
        if (!UtString.empty(s)) {
            return s;
        }
        throw new XError("Переменная не установлена: ${JcConsts.PROP_APP_DIR}")
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        //
        ctx = CtxFactory.createCtx()
    }

    protected void prepareRealCtx() {
        String root1 = UtFile.join(getJcAppdir(), "")
        ctx.addLibDir(root1 + "/_jc/lib-builder")
    }

    protected String basepath(String path) {
        String basepath = UtFile.join(getJcAppdir(), "p-jc/jandcode-jc/test.projects")
        return UtFile.join(basepath, path)
    }

    /**
     * Загрузить проект
     * @param path путь относительно test.projects
     */
    protected Project load(String path) {
        return ctx.load(basepath(path))
    }

    /**
     * Загрузить проект
     * @param path путь относительно test.projects
     * @param includeScript класс скрипта проекта для include
     */
    protected Project load(String path, Class includeScript) {
        Project p = load(path)
        p.include(includeScript.getName())
        return p
    }

    /**
     * Создает dummy скрипт для проекта для использования: <code>script(p).with {}</code>
     */
    public ProjectScript script(Project p) {
        DummyProjectScript sr = new DummyProjectScript()
        sr.setProject(p)
        return sr
    }

    /**
     * Имитация запуска jc в указанном каталоге с указанными аргументами
     */
    void run(String workdir, List args) {
        workdir = basepath(workdir)
        MainImpl main = new MainImpl();
        main.run(args as String[], workdir, workdir, true);
    }

    /**
     * Имитация запуска jc в указанном каталоге с указанными аргументами
     */
    void run(String workdir, String args) {
        String[] ar = args.split(" ")
        run(workdir, ar.toList())
    }

    /**
     * help для команды
     */
    void help(Project p, String cmName) {
        MainImpl main = new MainImpl()
        println(main.helpCm(p, p.cm.get(cmName)))
    }

    /**
     * help для проекта
     */
    void help(Project p) {
        MainImpl main = new MainImpl()
        println(main.help(p))
    }

}
