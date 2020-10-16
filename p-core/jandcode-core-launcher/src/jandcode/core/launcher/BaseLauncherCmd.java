package jandcode.core.launcher;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.core.*;

/**
 * Базовый класс для команд
 */
public abstract class BaseLauncherCmd extends BaseComp implements LauncherCmd {

    private CliArgs args = UtCli.createArgs(null);

    public CliArgs getArgs() {
        return args;
    }

    public void setArgs(CliArgs args) {
        this.args = args;
        if (this.args == null) {
            this.args = UtCli.createArgs(null);
        }
    }

    public boolean isVerbose() {
        return getApp().bean(LauncherService.class).isVerbose();
    }
}
