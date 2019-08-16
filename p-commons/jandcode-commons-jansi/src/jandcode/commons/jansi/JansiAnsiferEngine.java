package jandcode.commons.jansi;

import jandcode.commons.*;
import jandcode.commons.ansifer.*;
import org.fusesource.jansi.*;

public class JansiAnsiferEngine implements AnsiferEngine {

    private static boolean jansiInstalled = false;

    public boolean install() {
        try {
            if (!jansiInstalled) {

                // уюеждаемся, что jansi присутствует
                UtClass.getClass("org.fusesource.jansi.AnsiConsole");

                // извлекаем нативные библиотеки jansi
                JansiExtractor q = new JansiExtractor();
                String nativeLibPath = null;
                try {
                    nativeLibPath = q.extract();
                    if (!UtString.empty(nativeLibPath)) {
                        System.getProperties().setProperty("library.jansi.path", nativeLibPath);
                    }
                } catch (Exception e) {
                    // ignore
                }

                AnsiConsole.systemInstall();
                jansiInstalled = true;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void uninstall() {
        try {
            if (jansiInstalled) {
                AnsiConsole.systemUninstall();
                jansiInstalled = false;
            }
        } catch (Exception e) {
            //ignore
        }
    }

}
