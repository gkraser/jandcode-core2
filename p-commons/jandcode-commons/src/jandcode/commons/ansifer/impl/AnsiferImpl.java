package jandcode.commons.ansifer.impl;


import jandcode.commons.*;
import jandcode.commons.ansifer.*;

import java.util.*;

/**
 * ansi-краски в консоле
 */
public class AnsiferImpl implements Ansifer {

    private static String ESC = "\033[";
    private static String ESC_M = "\033[m";
    private static String defaultEngineClass = "jandcode.commons.jansi.JansiAnsiferEngine";

    private HashMap<String, AnsiferStyle> styles = new HashMap<>();
    private int levelOn;
    private AnsiferEngine engine;
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
        DefaultStyles.registerDefaultStyles(this);
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

    public boolean isOn() {
        return levelOn > 0;
    }

    public void ansiOn() {
        if (levelOn > 0) {
            levelOn++;
            return;
        }

        //
        if (getEngine().install()) {
            levelOn = 1;
        }
    }

    public void ansiOff() {
        if (levelOn > 1) {
            levelOn--;
            return;
        }

        //
        if (levelOn == 1) {
            getEngine().uninstall();
            levelOn = 0;
        }
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
        return new AnsiferStyleImpl(AnsiferColor.fromString(color), AnsiferColor.fromString(background));
    }

    public AnsiferStyle getStyle(AnsiferColor color, AnsiferColor background) {
        return new AnsiferStyleImpl(color, background);
    }

    public Collection<String> getStyleNames() {
        return styles.keySet();
    }

    public String color(AnsiferStyle style, String s) {
        if (s == null) {
            s = "";
        }
        if (!isOn()) {
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

    public String color(String styleOrColor, String s) {
        return color(getStyle(styleOrColor), s);
    }

    public String color(AnsiferColor color, AnsiferColor background, String s) {
        return color(getStyle(color, background), s);
    }

    public String color(String color, String background, String s) {
        return color(getStyle(color, background), s);
    }

}
