package jandcode.core.launcher.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.launcher.*;

public class CmdImpl implements Cmd {

    private App app;
    private String name;
    private String clsName;
    private Help help;

    public CmdImpl(App app, String name, String clsName) {
        this.app = app;
        this.name = name;
        this.clsName = clsName;
    }

    public String getName() {
        return name;
    }

    public Help getHelp() {
        if (this.help == null) {
            HelpBuilderImpl b = new HelpBuilderImpl();
            LauncherCmd z = createInst();
            try {
                z.help(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Opt opt : b.getOpts()) {
                if (LauncherConsts.isOptSys(opt.getName())) {
                    throw new XError("Опция {0} является системной и не может быть " +
                            "задействована в команде {1}", opt.getName(), clsName);
                }
            }
            this.help = b;
        }
        return help;
    }

    public LauncherCmd createInst() {
        BaseLauncherCmd res = (BaseLauncherCmd) app.create(this.clsName);
        res.setName(getName());
        return res;
    }

}


