package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Ut_Test extends CustomProjectTestCase {

    @Test
    public void test_runcmd() throws Exception {
        Project p = load("workdir1")
        script(p).with {
            def res = ut.runcmd(cmd: "hg", showout: false, saveout: true)
            println "----"
            println res
        }
    }

    @Test
    public void test_runcmd_error() throws Exception {
        Project p = load("workdir1")
        script(p).with {
            try {
                ut.runcmd(cmd: "hg cccc", showout: false, saveout: true)
                fail()
            } catch (ignore) {
            }
        }
    }


    @Test
    public void test_printMap() throws Exception {
        Project p = load("workdir1")
        def m = [
                'aaa'      : 'bbb',
                'vvv'      : [
                        'ddd1': 'ddsd',
                        'ddd2': ['ddsd'],
                        'ddd3': [
                                'ddd': 'ddsd',
                                'ddd': ['ddsd'],
                                'ddd': ['ddsd'],
                        ]
                ],
                'aaa123123': 'bbb2342342',
                'dd'       : [1, 2, 3, 4],
                'aaa1231'  : 'bbb231231232142342',
        ]
        script(p).with {
            ut.printMap(m)
        }
    }

    @Test
    public void test_cleandir() throws Exception {
        Project p = load("workdir1")
        script(p).with {
            def d = wd("temp/clean-dir-test")
            ant.mkdir(dir: d)
            ant.echo(message: "1", file: "${d}/f1.txt")
            ant.echo(message: "1", file: "${d}/f2/f1.txt")
            ant.echo(message: "1", file: "${d}/f2/.hgignore")
            //
            ut.cleandir(d)
            //
        }
    }

}
