package jandcode.jc.junit;

import org.junit.platform.console.*;

/**
 * Запуск org.junit.platform.console.ConsoleLauncher с особой настройкой для jc
 */
public class ConsoleLauncherWrap {

    public static void main(String[] args) {

        // скрываем вывод на консоль
        ConsoleWrap.wrap();

        // запускаем оригинал
        int exitCode = ConsoleLauncher.execute(System.out, System.err, args).getExitCode();
        System.exit(exitCode);
    }

}
