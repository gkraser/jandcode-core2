package jandcode.core.launcher;

import jandcode.commons.test.*;
import jandcode.core.launcher.impl.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class HelpBuilder_Test extends Utils_Test {

    @Test
    public void opts() throws Exception {
        HelpBuilderImpl b = new HelpBuilderImpl();

        b.desc("Это как бы помощь");
        b.opt("p", "Порт", "PORT", 8080);
        b.opt("c", "Контекст", "CONTEXT", "/jc");
        b.opt("a");
        b.opt("b", "help1\nhelp2");
        b.opt("d", "help1 help2", true);

        HelpPrinter p = new HelpPrinter();

        String s = p.options(b);

        System.out.println("[" + s + "]");

    }

    class DummyCmd implements Cmd {

        String name;
        Help help;

        public DummyCmd(String name, Help help) {
            this.name = name;
            this.help = help;
        }

        public Help getHelp() {
            return help;
        }

        public LauncherCmd createInst() {
            return null;
        }

        public String getName() {
            return name;
        }
    }

    private Cmd createDummyCmd(String name, String help) {
        HelpBuilderImpl b = new HelpBuilderImpl();
        b.desc(help);
        Cmd z = new DummyCmd(name, b);
        return z;
    }

    @Test
    public void cmds() throws Exception {

        List<Cmd> cmds = new ArrayList<>();
        cmds.add(createDummyCmd("command1", "Это типа помощь"));
        cmds.add(createDummyCmd("command2", "Это типа помощь2\nмного строк"));

        HelpPrinter p = new HelpPrinter();

        String s = p.cmds(cmds);

        System.out.println(s);

    }


}
