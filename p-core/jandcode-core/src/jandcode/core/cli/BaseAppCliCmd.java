package jandcode.core.cli;

import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.core.*;

/**
 * Базовый класс для команд, которые работают в контексте приложения.
 */
public abstract class BaseAppCliCmd implements CliCmd, IAppLinkSet, IAppLink {

    private App app;

    public App getApp() {
        if (app == null) {
            throw new XError("Не установлено свойство app для {0}. Необходимо подключить " +
                    "расширение AppCliExtinsion в CliLauncher");
        }
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

}
