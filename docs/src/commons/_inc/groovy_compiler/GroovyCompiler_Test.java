package commons._inc.groovy_compiler;

import jandcode.commons.*;
import jandcode.commons.groovy.*;
import jandcode.commons.test.*;
import org.apache.commons.vfs2.*;
import org.junit.jupiter.api.*;

public class GroovyCompiler_Test extends Utils_Test {

    UtFileWrapper UtFile = new UtFileWrapper();

    class UtFileWrapper {
        FileObject getFileObject(String name) {
            return jandcode.commons.UtFile.getFileObject(utils.getTestFile(name + ".txt"));
        }
    }

    @Test
    public void test1() throws Exception {
        //= 1
        GroovyCompiler compiler = UtGroovy.createCompiler();
        //= 2
        FileObject fo1 = UtFile.getFileObject("myscript.groovy"); // как-то получаем FileObject
        GroovyClazz c1 = compiler.getClazz(MyBaseClass.class, "void run()",
                fo1, false);
        MyBaseClass inst1 = (MyBaseClass) c1.createInst();
        inst1.run();
        //= 3
        GroovyClazz c2 = compiler.getClazz(MyBaseClass.class, "void run()",
                "print('hello!')", false);
        MyBaseClass inst2 = (MyBaseClass) c2.createInst();
        inst2.run();
        //=
    }

}
