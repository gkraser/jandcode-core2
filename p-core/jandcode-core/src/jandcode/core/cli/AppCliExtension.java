package jandcode.core.cli;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.core.*;

/**
 * Расширение {@link CliLauncher} для команд, которые работают в контексте приложения.
 */
public class AppCliExtension implements CliExtension, ICliLauncherLinkSet {

    private CliLauncher cliLauncher;
    private App app;
    private String appFile;

    public CliLauncher getCliLauncher() {
        return cliLauncher;
    }

    public void setCliLauncher(CliLauncher cliLauncher) {
        this.cliLauncher = cliLauncher;
    }

    /**
     * Файл app.cfx, откуда будет загружено приложение. По умолчанию не установлено,
     * считается что app.cfx в каталоге приложения.
     */
    public String getAppFile() {
        return appFile;
    }

    public void setAppFile(String appFile) {
        this.appFile = appFile;
    }

    protected String getAppFileAbs() {
        String s = getAppFile();
        if (UtString.empty(s)) {
            s = UtFile.join(getCliLauncher().getAppDir(), AppConsts.FILE_APP_CONF);
        } else {
            if (!UtFile.isAbsolute(s)) {
                s = UtFile.join(UtFile.getWorkdir(), s);
            }
        }
        return s;
    }

    //////

    public void beforeCmdExec(CliCmd cmd) {
        if (cmd instanceof IAppLinkSet) {
            ((IAppLinkSet) cmd).setApp(getApp());
        }
    }

    public void cliConfigure(CliDef b) {
        b.opt("appFile").names("-app").arg("FILE")
                .desc("Имя файла с конфигурацией приложения")
                .defaultValue(AppConsts.FILE_APP_CONF + " в каталоге приложения");
    }

    //////

    /**
     * Экземпляр приложения. Создается при первом вызове.
     */
    public App getApp() {
        if (app == null) {
            try {
                app = loadApp();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return app;
    }

    protected App loadApp() throws Exception {
        String appDir = getCliLauncher().getAppDir();
        String appFile = getAppFileAbs();
        if (!UtFile.exists(appFile)) {
            throw new XError("Файл приложения {0} не найден");
        }
        //
        return AppLoader.load(appFile, appDir, null, false);
    }

}
