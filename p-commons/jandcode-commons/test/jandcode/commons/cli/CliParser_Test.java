package jandcode.commons.cli;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CliParser_Test extends Utils_Test {

    CliParser create(String... args) {
        return UtCli.createCliParser(args);
    }

    @Test
    public void test1() throws Exception {
        CliParser p = create();
        assertEquals(p.getArgs().size(), 0);
    }

    @Test
    public void param() throws Exception {
        CliParser p = create("-a", "b", "-h", "c");
        assertEquals(p.extractParam(), "b");
        assertEquals(p.getArgs().toString(), "[-a, -h, c]");
    }

    @Test
    public void opts1() throws Exception {
        CliDef b = UtCli.createCliDef();
        //
        b.opt("p1").names("-p,--port").arg(true);
        b.opt("c1").names("-c,--context").arg("C1");
        b.opt("a1").names("-a").arg(true);
        b.opt("v1").names("-v");
        //
        CliParser p = create("-a", "b", "-h", "c", "-c=ctx", "--port", "9", "-p", "8");
        Map<String, Object> m = p.extractOpts(b);
        assertEquals(m.toString(), "{a1=b, c1=ctx, p1=8}");
        assertEquals(p.getArgs().toString(), "[-h, c]");
    }

    @Test
    public void opts2_multi() throws Exception {
        CliDef b = UtCli.createCliDef();
        //
        b.opt("p1").names("-p,--port").arg(true).multi(true);
        b.opt("v1").names("-v");
        //
        CliParser p = create("-a", "--port", "9", "-p", "8");
        Map<String, Object> m = p.extractOpts(b);
        assertEquals(m.toString(), "{p1=[9, 8]}");
        assertEquals(p.getArgs().toString(), "[-a]");
    }

    @Test
    public void opts3_positional() throws Exception {
        CliDef b = UtCli.createCliDef();
        //
        b.opt("p1").names("-p,--port").arg(true).multi(true);
        b.opt("v1").names("-v");
        b.opt("pos1");
        b.opt("pos2");
        b.opt("pos3").multi(true);
        //
        CliParser p = create("-a", "cmd1", "--port", "9", "cmd2", "cmd3", "-p", "8", "cmd4");
        Map<String, Object> m = p.extractOpts(b);
        assertEquals(m.toString(), "{pos1=cmd1, p1=[9, 8], pos2=cmd2, pos3=[cmd3, cmd4]}");
        assertEquals(p.getArgs().toString(), "[-a]");
    }

    @Test
    public void opts4_positional2() throws Exception {
        CliDef b = UtCli.createCliDef();
        //
        b.opt("p1").names("-p,--port").arg(true).multi(true);
        b.opt("v1").names("-v");
        b.opt("pos1");
        b.opt("pos2");
        b.opt("pos3");
        //
        CliParser p = create("-a", "cmd1", "--port", "9", "cmd2", "cmd3", "-p", "8", "cmd4");
        Map<String, Object> m = p.extractOpts(b);
        assertEquals(m.toString(), "{pos1=cmd1, p1=[9, 8], pos2=cmd2, pos3=cmd3}");
        assertEquals(p.getArgs().toString(), "[-a, cmd4]");
    }


}
