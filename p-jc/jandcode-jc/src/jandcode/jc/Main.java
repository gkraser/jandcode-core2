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
        if (!main.run(args, null, null, false)) {
            System.exit(1);
        }
    }

}
