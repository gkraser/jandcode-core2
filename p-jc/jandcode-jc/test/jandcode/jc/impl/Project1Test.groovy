package jandcode.jc.impl

import jandcode.jc.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Project1Test extends CustomProjectTestCase {

    @Test
    public void test_project1() throws Exception {
        Project p
        p = load("project1")
        p = load("project1")
        p = load("project1")
        //
        assertEquals(p.vars.counter, 1)
    }

    @Test
    public void test_project2() throws Exception {
        Project p
        p = load("project1/project2.jc")
        //
        assertEquals(p.vars.counter, 2)
    }

    @Test
    public void test_project3() throws Exception {
        Project p
        p = load("project1/project3.jc")
        //
    }

}
