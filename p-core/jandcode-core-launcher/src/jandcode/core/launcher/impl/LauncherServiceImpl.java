package jandcode.core.launcher.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.launcher.*;

import java.util.*;

public class LauncherServiceImpl extends BaseComp implements LauncherService {

    private boolean verbose;
    private NamedList<Cmd> cmds = new DefaultNamedList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        this.cmds = new DefaultNamedList<>();
        Collection<Conf> lst = getApp().getConf().getConfs("launcher/cmd-provider");
        for (Conf x : lst) {
            CmdProvider p = (CmdProvider) getApp().create(x);
            Collection<Cmd> tmpCmds = p.loadCmds();
            if (tmpCmds != null) {
                cmds.addAll(tmpCmds);
            }
        }

    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public NamedList<Cmd> getCmds() {
        return cmds;
    }

    public Cmd findCmd(String name) {
        return cmds.find(name);
    }

}
