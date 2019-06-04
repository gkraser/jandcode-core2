package jandcode.commons.ansifer4.impl;


import jandcode.commons.*;
import jandcode.commons.ansifer4.*;

import java.util.*;

/**
 * ansi-краски в консоле
 */
public class AnsiferImpl implements Ansifer {

    private static String ESC = "\033[";
    private static String ESC_M = "\033[m";
    private static String defaultEngineClass = "jandcode.jcincubator.ansifer4.jansi.JansiAnsiferEngine";

    private HashMap<String, AnsiferStyle> styles = new HashMap<>();
    private boolean installed;
    private AnsiferEngine engine;
    private boolean enabled = true;
    private HashMap<AnsiferColor, String> colorTable = new HashMap<>();
    private HashMap<AnsiferColor, String> backgroundTable = new HashMap<>();

    public AnsiferImpl() {
        for (AnsiferColor c : AnsiferColor.values()) {
            backgroundTable.put(c, ESC + "4" + c.code() + "m");
            if (!c.bright()) {
                colorTable.put(c, ESC + "3" + c.code() + "m");
            } else {
                colorTable.put(c, ESC + "3" + c.code() + "m" + ESC + "1m");
            }
        }
    }

    public AnsiferEngine getEngine() {
        if (engine == null) {
            synchronized (this) {
                if (engine == null) {
                    try {
                        engine = (AnsiferEngine) UtClass.createInst(defaultEngineClass);
                    } catch (Exception e) {
                        engine = (AnsiferEngine) UtClass.createInst(DummyAnsiferEngine.class);
                    }
                }
            }
        }
        return engine;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void install() {
        if (installed) {
            return;
        }

        //
        if (getEngine().install()) {
            installed = true;
        }
    }

    public void uninstall() {
        if (!installed) {
            return;
        }

        //
        if (getEngine().uninstall()) {
            installed = false;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void registerStyle(String styleName, AnsiferColor color, AnsiferColor background, boolean defaultStyle) {
        if (defaultStyle) {
            if (styles.containsKey(styleName)) {
                return;
            }
        }
        styles.put(styleName, new AnsiferStyleImpl(color, background));
    }

    public AnsiferStyle getStyle(String styleName) {
        AnsiferStyle st = styles.get(styleName);
        if (st != null) {
            return st;
        }
        AnsiferColor color = AnsiferColor.fromString(styleName);
        if (color != null) {
            return new AnsiferStyleImpl(color, null);
        }
        return null;
    }

    public AnsiferStyle getStyle(String color, String background) {
        //todo оптимизация-кеширование?
        return new AnsiferStyleImpl(AnsiferColor.fromString(color), AnsiferColor.fromString(background));
    }

    public Collection<String> getStyleNames() {
        return styles.keySet();
    }

    public String color(AnsiferStyle style, String s) {
        if (s == null) {
            s = "";
        }
        if (!installed || !enabled) {
            return s;
        }

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
