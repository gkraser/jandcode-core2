package xxx.yyy.main;

import jandcode.commons.cli.*;
import jandcode.core.cli.*;

public class Main {

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        CliLauncher z = new CliLauncher(args);
        z.addExtension(new AppCliExtension());
        z.addCmd("cmd", new Cmd());
        z.exec();
    }

    public static class Cmd extends BaseAppCliCmd {

        public void exec() throws Exception {
            System.out.println("cmd exec");
        }

        public void cliConfigure(CliDef b) {
        }

    }
}
