package jandcode.commons.io;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TextParser_Test extends Utils_Test {
    TextParser x;

    class MyParser1 extends TextParser {
        protected void onParse() throws Exception {
            check('h', 1, 1, 0);
            next();
            check('h', 1, 1, 0);
            next();
            check('\n', 1, 2, 1);
            next();
            check('e', 2, 1, 2);
            next();
            check('\n', 2, 2, 3);

            assertFalse(last == EOF);
            for (int i = 0; i < 100; i++) {
                next();
            }
            assertTrue(last == EOF);
        }
    }

    class MyParser_Tab extends TextParser {
        StringBuilder data = new StringBuilder();

        protected void onParse() throws Exception {
            while (true) {
                char c = next();
                if (c == EOF)
                    break;
                data.append(c);
            }
        }
    }

    private void check(char c, int row, int col, int pos) {
        assertEquals(c, x.last);
        assertEquals(row, x.getRow(), "row");
        assertEquals(col, x.getCol(), "col");
        assertEquals(pos, x.getPos(), "pos");
    }

    @Test
    public void testParse() throws Exception {
        x = new MyParser1();
        String s = "h\ne\nllo";
        UtLoad.fromString(x, s);
    }

    @Test
    public void testParseFile() throws Exception {

        class MyParser extends TextParser {
            protected void onParse() throws Exception {
                char c = next();
                assertEquals('H', c);
                assertEquals('H', last);

                while (last != EOF) {
                    next();
                }
                check(EOF, 3, 7, 21);
            }
        }

        x = new MyParser();
        String s = "HELLO\r\n" +
                "PARSER\r\n" +
                "--END--";
        UtLoad.fromString(x, s);


    }


}