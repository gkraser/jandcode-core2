package jandcode.mdoc

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.mdoc.builder.*
import org.junit.jupiter.api.*

class Toc1_Test extends CustomMDoc_Test {

    void printToc(String tocFile, String docName = "toc1") {
        OutBuilder b = mdoc.loadDoc(docName).createBuilder("html")

        SimXml x = new SimXmlNode()
        x.load().fromString(b.doc.getSourceFiles().get(tocFile).getText())

        XmlTocBuilder tocBuilder = new XmlTocBuilder(b, x)
        def toc = tocBuilder.buildToc(UtFile.path(tocFile))

        mdoc.printToc(toc)

    }

    @Test
    public void toc1() throws Exception {
        printToc("toc1.xml")
    }

    @Test
    public void toc2() throws Exception {
        printToc("toc2.xml")
    }

    @Test
    public void toc3() throws Exception {
        printToc("toc3.xml")
    }

    @Test
    public void part1() throws Exception {
        printToc("part1/toc1.xml")
    }

    @Test
    public void part2() throws Exception {
        printToc("part2/toc1.xml")
    }


}
