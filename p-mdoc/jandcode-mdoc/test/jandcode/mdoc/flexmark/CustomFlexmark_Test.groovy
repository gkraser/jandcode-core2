package jandcode.mdoc.flexmark


import com.vladsch.flexmark.util.ast.*
import jandcode.commons.test.*
import jandcode.mdoc.flexmark.mdtopic.*

abstract class CustomFlexmark_Test extends Utils_Test {

    FlexmarkEngine engine;

    void setUp() throws Exception {
        super.setUp();
        //
        engine = new FlexmarkEngine()
    }

    String parse(String mdText, boolean doPrint = true) {
        Document doc = engine.getParser().parse(mdText)
        String html = engine.getRenderer().render(doc)
        if (doPrint) {
            utils.delim(getTestName())
            println html
            utils.delim('', '-')
        }
        return html
    }

    MdTopic parseTopic(String mdText, boolean doPrint = true) {
        MdTopic t = engine.createMdTopic(mdText)
        if (doPrint) {
            utils.delim(getTestName())
            println "document title=[${t.getTitle()}]"
            println "document props=[${t.getProps()}]"
            printToc(t.toc, "")
            println "==="
            println t.body
        }
        return t
    }

    void printToc(MdToc toc, String ind) {
        println "${ind}|title=[${toc.title}]id=[${toc.id}]"
        for (MdToc x : toc.childs) {
            printToc(x, ind + "| ")
        }
    }
}
