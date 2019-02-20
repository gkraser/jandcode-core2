package jandcode.commons.ansifer.impl;

import jandcode.commons.*;
import jandcode.commons.ansifer.*;
import org.fusesource.jansi.*;

import java.util.*;

public class AnsiferProviderDefault implements AnsiferProvider {

    private static boolean jansiInstalled = false;

    private static String ESC = "\033[";
    private static String ESC_M = "\033[m";

    private HashMap<AnsiferColor, String> colorTable = new HashMap<>();
    private HashMap<AnsiferColor, String> backgroundTable = new HashMap<>();

    public AnsiferProviderDefault() {
        for (AnsiferColor c : AnsiferColor.values()) {
            backgroundTable.put(c, ESC + "4" + c.code() + "m");
            if (!c.bright()) {
                colorTable.put(c, ESC + "3" + c.code() + "m");
            } else {
                colorTable.put(c, ESC + "3" + c.code() + "m" + ESC + "1m");
            }
        }
    }

    public boolean install() {
        try {
            if (!jansiInstalled) {

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

    public boolean uninstall() {
        try {
            if (jansiInstalled) {
                AnsiConsole.systemUninstall();
                jansiInstalled = false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String style(AnsiferStyle style, String s) {
        if (style == null) {
            return s;
        }
        String c = colorTable.get(style.getColor());
        String b = backgroundTable.get(style.getBackground());
        if (c != null && b != null) {
            return c + b + s + ESC_M;
        } else if (c != null) {
            return c + s + ESC_M;
        } else if (b != null) {
            return b + s + ESC_M;
        }
        return s;
    }

}
