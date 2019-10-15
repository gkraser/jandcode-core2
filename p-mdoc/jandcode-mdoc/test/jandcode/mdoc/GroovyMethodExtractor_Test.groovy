package jandcode.mdoc

import jandcode.mdoc.groovy.*
import org.junit.jupiter.api.*

class GroovyMethodExtractor_Test extends CustomMDoc_Test {

    @Test
    public void extractMethodBodys1() throws Exception {
        GroovyMethodExtractor z = new GroovyMethodExtractor()
        Map res = z.extractMethodBodys("""
class AAA {

    void m1() {
       println "2"
    }

    void m2() { println "2" }

    void m3() {
    
    
       println "1"
       println "2"
       println "3"
    }

}
""")
        println res
    }

    @Test
    public void extractMethodBodysWithAnnotation1() throws Exception {
        GroovyMethodExtractor z = new GroovyMethodExtractor()
        Map res = z.extractMethodBodys("""
class AAA {

    @Test
    void m1() {
       println "2"
    }

}
""")
        println res
    }


}
