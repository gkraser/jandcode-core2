package jandcode.mdoc

import jandcode.mdoc.builder.*
import jandcode.mdoc.html.*
import jandcode.mdoc.topic.*
import org.junit.jupiter.api.*

class ImgRefPostProcessor_Test extends CustomMDoc_Test {

    @Test
    public void test1() throws Exception {
        def builder = mdoc.buildDoc("doc1")

        Topic t = builder.doc.topics.find("img/index")
        println t.body

        //
        def p = builder.create(ImgRefPostProcessor)
        OutFile f = new OutFile(t.sourceFile.path + ".html", t.sourceFile, t)
        String s = p.process(t.getBody(), f)
        println s
    }


}
