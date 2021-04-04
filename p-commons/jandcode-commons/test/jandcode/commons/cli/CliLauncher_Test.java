package jandcode.commons.cli;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

public class CliLauncher_Test extends Utils_Test {

    CliLauncher create(String... args) {
        CliLauncher z = new CliLauncher(args);
        z.setExitOnError(false);
        z.cmdName("test1");
        z.version("1.0.1");
        z.desc("Desc for app");
        z.footer("Footer for app");
        return z;
    }

    class Cmd1 implements CliCmd {

        String tag;

        public Cmd1(String tag) {
            this.tag = tag == null ? "one" : tag;
        }

        public void exec() throws Exception {
            System.out.println("exec cmd1: " + tag);
        }

        public void cliConfigure(CliDef b) {
            b.cmdName("cmd1-name");
            b.desc("Cmd1: Desc " + tag);
            b.footer("Cmd1: Footer" + tag);
            b.opt("opt1").names("-1").desc("Opt 1");
            b.opt("opt2").names("-2").arg(true).desc("Opt 2");
        }

        private boolean opt1;
        private String opt2 = "value0";

        public boolean isOpt1() {
            return opt1;
        }

        public void setOpt1(boolean opt1) {
            this.opt1 = opt1;
        }

        public String getOpt2() {
            return opt2;
        }

        public void setOpt2(String opt2) {
            this.opt2 = opt2;
        }
    }

    @Test
    public void one_cmd_exec() throws Exception {
        CliLauncher z = create();
        z.exec(new Cmd1(null));
    }

    @Test
    public void one_cmd_help() throws Exception {
        CliLauncher z = create("-h");
        z.exec(new Cmd1(null));
    }

    @Test
    public void multi_cmd_exec_no_command() throws Exception {
        CliLauncher z = create();
        z.addCmd("cmd1", new Cmd1("1"));
        z.addCmd("cmd2", new Cmd1("2"));
        z.exec();
    }

    @Test
    public void multi_cmd_help() throws Exception {
        CliLauncher z = create("cmd2", "-h");
        z.addCmd("cmd1", new Cmd1("1"));
        z.addCmd("cmd2", new Cmd1("2"));
        z.exec();
    }

    @Test
    public void multi_cmd_exec() throws Exception {
        CliLauncher z = create("cmd2");
        z.addCmd("cmd1", new Cmd1("1"));
        z.addCmd("cmd2", new Cmd1("2"));
        z.exec();
    }

}
