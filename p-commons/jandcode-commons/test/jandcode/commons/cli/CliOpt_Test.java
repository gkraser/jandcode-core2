package jandcode.commons.cli;

import jandcode.commons.cli.impl.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CliOpt_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        CliOptImpl z = new CliOptImpl("opt1");
        z.names("-d,--debug").desc("Отладка");
        //
        assertEquals(z.getKey(), "opt1");
        //
        assertEquals(z.getNames().toString(), "[-d, --debug]");
        assertEquals(z.getDesc(), "Отладка");
        assertEquals(z.hasArg(), false);
        assertEquals(z.hasDefaultValue(), false);
        //
        z.arg(true);
        assertEquals(z.hasArg(), true);
        assertEquals(z.getArg(), "ARG");
        //
        z.arg(false);
        assertEquals(z.hasArg(), false);
        assertEquals(z.getArg(), null);
        //
        assertEquals(z.hasDefaultValue(), false);
        assertEquals(z.getDefaultValue(), null);
        z.defaultValue(123);
        assertEquals(z.hasDefaultValue(), true);
        assertEquals(z.getDefaultValue(), "123");
        //
    }

    @Test
    public void holder_builder_and_print_help() throws Exception {
        CliDef b = new CliDefImpl();
        //
        b.opt("port").desc("Port").names("-p,--port").arg("PORT").defaultValue("8080");
        b.opt("context").desc("Context").names("-c").arg(true);
        b.opt("verbose").desc("Verbose\nverbose2").names("-v");
        b.opt("use").desc("Use").names("--use-this");
        b.opt("all").names("-a");
        //
        CliHelpUtils hm = new CliHelpUtils();
        String s = hm.getOptHelp(b.getOpts());
        //
        System.out.println(s);
    }


}
