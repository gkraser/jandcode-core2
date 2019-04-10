package jandcode.jc;

import jandcode.jc.impl.*;

/**
 * Запуск cli
 */
public class Main {

    /**
     * Запуск утилиты с командной строки. Вызывается из os.
     */
    public static void main(String[] args) throws Exception {
        MainImpl main = new MainImpl();
        String appdir = System.getProperty(JcConsts.PROP_APP_DIR);
        if (!main.run(args, appdir, null, false)) {
            System.exit(1);
        }
    }

}
