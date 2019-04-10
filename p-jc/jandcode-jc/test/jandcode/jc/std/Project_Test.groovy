package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

/**
 * Тесты для проекта
 */
class Project_Test extends CustomProjectTestCase {

    @Test
    public void load_absPath() throws Exception {
        Project p = load("workdir1")

        String p1 = p.wd("../main1")
        println p1

        String pp = p.wd(p1)
        println(pp)

        ProjectScript ps = script(p)
        Project p2 = ps.load(p1)
        println p2.wd()
    }


    @Test
    public void include_notExists() throws Exception {
        Project p = load("workdir1")
        p.include(p.wd("../main1/project.jc"))
    }


}
