package jandcode.web.gsp

import jandcode.web.*
import jandcode.web.test.*
import jandcode.web.virtfile.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Gsp_Test extends Web_Test {

    WebService svc

    void setUp() throws Exception {
        super.setUp()

        svc = app.bean(WebService)
    }

    String render(String gspName, Map args) {
        GspContext ctx = svc.createGspContext();
        ITextBuffer b = ctx.render(gspName, args)
        String s = b.toString()
        utils.delim(gspName)
        println s
        utils.delim("gsp: " + gspName)
        println svc.getGspSourceText(gspName)
        utils.delim("groovy: " + gspName)
        println svc.getGspClassText(gspName)
        return s
    }

    @Test
    public void test_fromReg() throws Exception {
        assertEquals(render("gsp1", [a: 1, b: 2]),
                "Gsp1 [gsp1], args: [a:1, b:2]"
        )
        assertEquals(render("a/gsp1", [a: 11, b: 12]),
                "Gsp1 [a/gsp1], args: [a:11, b:12]"
        )
    }

    @Test
    public void test_fromFileObject() throws Exception {
        assertEquals(render(utils.getTestFile("data/f1.gsp"), [a: 1, b: 2]),
                "Gsp1 [f1.gsp], args: [a:1, b:2]"
        )
    }

    @Test
    public void test_fromVirtFile() throws Exception {
        assertEquals(render("data/f1.gsp", [a: 1, b: 2]),
                "Gsp1 [f1.gsp], args: [a:1, b:2]"
        )
    }

    @Test
    public void test_findFileTml() throws Exception {
        WebService fsvc = app.bean(WebService)
        VirtFile f

        FileType gspFt = fsvc.findFileType("gsp")
        assertNotNull(gspFt)
        assertEquals(gspFt.isTml(), true)

        f = fsvc.findFile("data/f1.gsp")
        assertEquals(f.path, "data/f1.gsp")
        f = fsvc.findFile("data/f2.html")
        assertEquals(f.path, "data/f2.html")
        assertEquals(f.fileType, "gsp")
        assertEquals(f.contentFileType, "html")

    }

    @Test
    public void test_relpath_in_gsp_1() throws Exception {
        String s
        s = web.renderGsp("relpath_in_gsp.html")
        assertEquals(s, "<script src=\"/test/js/js1.js\"></script>\n" +
                "<script src=\"/test/js/js1.js\"></script>\n" +
                "<script src=\"/test/js/js1.js\"></script>\n" +
                "<script src=\"/test/js/js1.js\"></script>")

        s = web.renderGsp("js/relpath_in_gsp.html")
        assertEquals(s, "<script src=\"/test/js/js/js1.js\"></script>\n" +
                "<script src=\"/test/js/js1.js\"></script>\n" +
                "<script src=\"/test/js/js1.js\"></script>\n" +
                "<script src=\"/test/js/js1.js\"></script>")
    }


}
