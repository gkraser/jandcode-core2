package jandcode.jc

import groovy.text.*
import jandcode.commons.test.*
import org.junit.jupiter.api.*

class Groovy_Test extends Utils_Test {

    class B {
        String a = "eee"
        String b = "ddd"
    }


    @Test
    public void test1() throws Exception {
        def a = '123 $a 456 ${b} 789'
        println a
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(a).make([a: 'sssn', b: 'dfff']).toString()
        println template
    }


}
