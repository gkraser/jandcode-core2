package jandcode.core.apex.ajc;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.jc.*;
import jandcode.jc.impl.*;

/**
 * Запускалка jc в product-режиме
 */
public class ApexJcMain extends MainProduct {

    /**
     * Переменная контекста с именем файла app.cfx, если он был передан в -app:file
     */
    public static final String VAR_APP_FILE = ApexJcMain.class.getName() + "#appFile";

    public void run(String[] args, String mainInclude) {
        setMainInclude(mainInclude);
        String appdir = System.getProperty(JcConsts.PROP_APP_DIR);
        if (UtString.empty(appdir)) {
            appdir = UtFile.getWorkdir();
        }
        if (!run(args, appdir, null, false)) {
            System.exit(1);
        }
    }

    protected void commonOptBuild(CliHelp z) {
        super.commonOptBuild(z);
        z.addOption("app", "Файл приложения (по умолчанию app.cfx)", true);
    }

    protected void grabOpt_other() {
        super.grabOpt_other();
        //
        String opt = "app";
        if (cli.containsKey(opt)) {
            String appFile = cli.getString("app");
            if (UtString.empty(appFile)) {
                throw new XError("В параметре app не указан файл");
            }
            appFile = UtFile.abs(appFile);
            if (!UtFile.exists(appFile)) {
                throw new XError("Файл не существует: {0}", appFile);
            }

            ctx.getVars().put(VAR_APP_FILE, appFile);

            cli.remove(opt);
        }
    }

}
