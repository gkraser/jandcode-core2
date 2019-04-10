package jandcode.mdoc

import jandcode.commons.simxml.*
import jandcode.mdoc.builder.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class XmlTocBuilder_Test extends CustomMDoc_Test {

    void printToc(String s, String docName = "real1") {
        OutBuilder b = mdoc.loadDoc(docName).createBuilder("html")

        SimXml x = new SimXmlNode()
        x.load().fromString(s)

        XmlTocBuilder tocBuilder = new XmlTocBuilder(b, x)
        def toc = tocBuilder.buildToc()

        mdoc.printToc(toc)

    }

    @Test
    public void test2() throws Exception {
        // language=XML
        String s = """<root>
    <toc-root topic="index">
         <toc topic="jc/index.md">
            <toc topic="jc/jc-bat"/>
            <toc topic="**/mdoc/**/*" type="plain"/>
            <toc topic="jc/gradle-tools"/>
            <toc topic="**/*" type="files"/>
         </toc>
    </toc-root>
</root>
"""
        printToc(s)
    }

    @Test
    public void test_autoFilesAll() throws Exception {
        // language=XML
        String s = """<root>
    <toc-root topic="index">
        <toc topic="**/*" type="auto"/>
    </toc-root>
</root>
"""
        printToc(s)
    }

    @Test
    public void test_autoAll2() throws Exception {
        // language=XML
        String s = """<root>
    <toc-root topic="index">
        <toc topic="**/*" type="auto"/>
    </toc-root>
</root>
"""
        printToc(s, "real2")
    }

    @Test
    public void test_autoFilesAll2() throws Exception {
        // language=XML
        String s = """<root>
    <toc-root topic="index">
        <toc topic="**/*" type="files"/>
    </toc-root>
</root>
"""
        printToc(s, "real2")
    }

    @Test
    public void test_noChildInInclude() throws Exception {
        // language=XML
        String s = """<root>
    <toc-root topic="index">
        <toc topic="**/*" type="auto">
            <toc/>
        </toc>
    </toc-root>
</root>
"""
        try {
            printToc(s, "real2")
            fail()
        } catch (e) {
            utils.showError(e)
        }
    }

    @Test
    public void test_relative1() throws Exception {
        // language=XML
        String s = """<root>
    <toc-root topic="index">
        <toc topic="commons/index">
            <toc topic="./pathprop"/>
            <toc topic="./*"/>
        </toc>
    </toc-root>
</root>
"""
        printToc(s, "real2")
    }

}
