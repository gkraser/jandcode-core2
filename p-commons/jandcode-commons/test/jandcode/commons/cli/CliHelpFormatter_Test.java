package jandcode.commons.cli;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CliHelpFormatter_Test extends Utils_Test {

    @Test
    public void testOpt() throws Exception {
        CliHelpFormatter d = UtCli.createHelpFormatter();
        d.addOpt("c", "asd", false);
        d.addOpt("cxcv", "dfg", true);
        d.addOpt("azx", "dfger", true);
        assertEquals(d.build(), "  -azx:ARG   dfger\n" +
                "  -c         asd\n" +
                "  -cxcv:ARG  dfg");
    }

    @Test
    public void testPar() throws Exception {
        CliHelpFormatter d = UtCli.createHelpFormatter();
        d.addCmd("c", "asd");
        d.addCmd("cxcv", "dfg");
        d.addCmd("azx", "dfger");
        assertEquals(d.build(), "  azx   dfger\n" +
                "  c     asd\n" +
                "  cxcv  dfg");
    }

    @Test
    public void testOptDuplicate() throws Exception {
        CliHelpFormatter d = UtCli.createHelpFormatter();
        d.addOpt("c", "asd", false);
        d.addOpt("cxcv", "dfg", true);
        d.addOpt("azx", "dfger", true);
        d.addOpt("c", "azz", true);
        assertEquals(d.build(), "  -azx:ARG   dfger\n" +
                "  -c:ARG     azz\n" +
                "  -cxcv:ARG  dfg");
    }

    @Test
    public void testNextLineHelp() throws Exception {
        CliHelpFormatter d = UtCli.createHelpFormatter();
        d.setHelpToNextLine(true);
        d.addCmd("c", "asd");
        d.addCmd("cxcv", "dfg");
        d.addCmd("azx", "dfger");
        assertEquals(d.build(), "  azx \n" +
                "        dfger\n" +
                "  c   \n" +
                "        asd\n" +
                "  cxcv\n" +
                "        dfg");
    }

    @Test
    public void testNextLineHelpLong() throws Exception {
        CliHelpFormatter d = UtCli.createHelpFormatter();
        d.setHelpToNextLine(true);
        d.addCmd("c123456789", "asd\n123\n321");
        assertEquals(d.build(), "  c123456789\n" +
                "        asd\n" +
                "        123\n" +
                "        321");
    }

    @Test
    public void test_trim() throws Exception {
        String ESC = "\033[";
        String s = ESC + "123";
        assertEquals(s.length(), 5);
        String s1 = s.trim();
        assertEquals(s1.length(), 4); //!!!!!! esc тоже убирается !!!!
    }

}
