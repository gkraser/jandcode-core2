package jandcode.commons.cli;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CliHelp_Test extends Utils_Test {

    @Test
    public void testOpt() throws Exception {
        CliHelp d = new CliHelp();
        d.addOption("c", "asd", false);
        d.addOption("cxcv", "dfg", true);
        d.addOption("azx", "dfger", true);
        assertEquals(d.toString(), "  -azx:ARG   dfger\n" +
                "  -c         asd\n" +
                "  -cxcv:ARG  dfg");
    }

    @Test
    public void testPar() throws Exception {
        CliHelp d = new CliHelp();
        d.addParam("c", "asd");
        d.addParam("cxcv", "dfg");
        d.addParam("azx", "dfger");
        assertEquals(d.toString(), "  azx   dfger\n" +
                "  c     asd\n" +
                "  cxcv  dfg");
    }

    @Test
    public void testOptDuplicate() throws Exception {
        CliHelp d = new CliHelp();
        d.addOption("c", "asd", false);
        d.addOption("cxcv", "dfg", true);
        d.addOption("azx", "dfger", true);
        d.addOption("c", "azz", true);
        assertEquals(d.toString(), "  -azx:ARG   dfger\n" +
                "  -c:ARG     azz\n" +
                "  -cxcv:ARG  dfg");
    }

    @Test
    public void testNextLineHelp() throws Exception {
        CliHelp d = new CliHelp();
        d.setHelpToNextLine(true);
        d.addParam("c", "asd");
        d.addParam("cxcv", "dfg");
        d.addParam("azx", "dfger");
        assertEquals(d.toString(), "  azx \n" +
                "        dfger\n" +
                "  c   \n" +
                "        asd\n" +
                "  cxcv\n" +
                "        dfg");
    }

    @Test
    public void testNextLineHelpLong() throws Exception {
        CliHelp d = new CliHelp();
        d.setHelpToNextLine(true);
        d.addParam("c123456789", "asd\n123\n321");
        assertEquals(d.toString(), "  c123456789\n" +
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
