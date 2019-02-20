package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

class Projecttml_Test extends CustomProjectTestCase {

    @Test
    public void test_listTml() throws Exception {
        run("workdir1", ['create', '-l'])
    }

    @Test
    public void test_helpTml() throws Exception {
        run("workdir1", ['create', '-t:tml1-tml2', '-h', '-v'])
    }

    @Test
    public void test_gen1() throws Exception {
        String outdir = basepath("workdir1/temp/gen1-gen2")
        Project p = load("workdir1")
        script(p).with {
            ant.delete(dir: outdir)
        }
        run("workdir1", "create -t:tml1-tml2 -v -o:${outdir}")
    }


}
