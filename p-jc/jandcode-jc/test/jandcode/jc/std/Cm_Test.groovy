package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

class Cm_Test extends CustomProjectTestCase {

    Project p

    @Test
    public void test1() throws Exception {
        p = load("cm_annotation1")
        help(p)
        help(p, "with-opt1")
    }

    @Test
    public void run_cms() throws Exception {
        p = load("cm_annotation1")
        p.cm.exec("cm1", [a1: 1])
        p.cm.exec("cm2", [a2: 2])
        p.cm.exec("cm3", [a3: 3])
        p.cm.exec("run-do1", [a4: 4])
        p.cm.exec("run-this-cm", [a5: 5])
        p.cm.exec("cm-over", [a6: 6])
        p.cm.exec("with-opt1", [a7: 7])
    }


}
