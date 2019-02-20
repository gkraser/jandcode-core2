package jandcode.commons.groovy

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.io.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class ScriptsTest extends GroovyModuleTestCase {

    public static class Base {

        def f1
        def f2
        def f3

        void run() {
        }

        String toString() {
            return "f1:${f1},f2:${f2},f3:${f3}"
        }

        void out(a) {
            if (f3 == null) f3 = ""
            f3 = f3 + a.toString().replace("\n", "").trim()
        }

    }

    void setUp() {
        super.setUp()
        getCompiler().setCompiledCacheDir("temp/ScriptsTest.CACHE")
        utils.logOn()
    }

    /**
     * Создать скрипт из текста
     */
    Base cr(String text, boolean template = false) {
        def c = getCompiler().getClazz(Base, "void run()", text, template)
        return (Base) c.createInst()
    }

    //////

    @Test
    public void test1() throws Exception {
        Base z = cr("""
print 'hello'
""")
        z.run()
    }

    @Test
    public void testErrorSource1() throws Exception {

        def check = { text, template, lineNumClass, line, lineText, lineTextPrepared, printclass = false ->
            GroovyClazz cg = getCompiler().getClazz(Base, "void run()", text, template)
            if (printclass) {
                utils.delim("source-class")
                println cg.getSourceClazz()
            }
            ErrorSource es = cg.createErrorSource(lineNumClass);
            assertEquals(es.getLineNum(), line, "linenum")
            assertEquals(es.getLineText(), lineText, "linetext")
            assertEquals(es.getLineTextPrepared(), lineTextPrepared, "linetextprepared")
        }

        check("hello", false, 4, 1, "hello", null)

        check("""
hello
hello2
""", false, 6, 3, "hello2", null)

        check("""import jandcode.commons.*
import jandcode.commons.groovy.*
hello
hello2
""", false, 7, 4, "hello2", null)

        check("""<%import jandcode.commons.*
import jandcode.commons.groovy.*%>
hello
hello2
""", true, 7, 4, "hello2", """;out("hello2\\n");""")

    }

    @Test
    public void testErrorCompile1() throws Exception {
        try {
            Base z = cr("""
  print 'hello'
  a=new AAA()
  print 'hello2'
  """)
        } catch (e) {
            utils.showError(e)
        }

    }

    @Test
    public void testErrorExecute1() throws Exception {
        try {
            Base z = cr("""
  import jandcode.commons.*
  import jandcode.commons.groovy.*
  print 'hello'
  a=a+1
  print 'hello2'
  """)
            z.run()
        } catch (e) {
            utils.showError(e)
        }

    }

    @Test
    public void testFileCompileInCache() throws Exception {
        def c = getCompiler().getClazz(Base, "void run()",
                UtFile.getFileObject("res:jandcode/commons/groovy/gdata/test1.txt"), false)
    }

    @Test
    public void testChanged() throws Exception {
        String text = "print 'hello'"
        StringSaver sv = new StringSaver(text)
        UtSave.toFile(sv, "temp/testChanged.groovy")
        def c = getCompiler().getClazz(Base, "void run()",
                UtFile.getFileObject("temp/testChanged.groovy"), false)
        def inst = c.createInst()
        inst.run()

        Thread.sleep(200)
        sv = new StringSaver("print 'hello2'")
        UtSave.toFile(sv, "temp/testChanged.groovy")

        UtGroovy.checkChangedResource()
        inst = c.createInst()
        inst.run()
        //

    }

    @Test
    public void testFindClazz1() throws Exception {
        String s = """
  import jandcode.commons.*
  import jandcode.commons.groovy.*
  print 'hello'
  print 'hello2'
  """
        Base z = cr(s)
        GroovyClazz x
        x = UtGroovy.findClazz(z.getClass())
        assertEquals(s, x.getSourceOriginal())
    }

    @Test
    public void testAssert1() throws Exception {
        String s = """
  assert 1==1
  """
        Base z = cr(s)
        z.run()
    }

    @Test
    public void testTZ() throws Exception {
        String s = """;f1=1;"""
        Base z = cr(s)
        z.run()
    }

}
