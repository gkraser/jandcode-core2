package jandcode.core.web.std;

import jandcode.commons.*;
import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Link_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        String s;

        s = web.renderGsp("jc/link", UtCnv.toMap("path", "a.css"));
        assertEquals(s, "<link rel=\"stylesheet\" href=\"/test/a.css\"/>");

        s = web.renderGsp("jc/link", UtCnv.toMap("path", "a.css?id=1"));
        assertEquals(s, "<link rel=\"stylesheet\" href=\"/test/a.css?id=1\"/>");

        s = web.renderGsp("jc/link", UtCnv.toMap("path", "a.js"));
        assertEquals(s, "<script src=\"/test/a.js\"></script>");

        s = web.renderGsp("jc/link", UtCnv.toMap("path", "a.js?id=1"));
        assertEquals(s, "<script src=\"/test/a.js?id=1\"></script>");
    }


}
