package jandcode.commons.ansifer;

import jandcode.commons.*;

import java.util.*;

/**
 * Печать примеров на консоле
 */
public class AnsiferSamplePrint {

    public static void main(String[] args) {
        printAllColors();
    }

    /**
     * Печать всех цветов
     */
    public static void printAllColors() {

        Ansifer a = UtAnsifer.getAnsifer();
        a.ansiOn();
        try {
            System.out.println();
            System.out.println();
            System.out.println(makeColorTable(a));
            System.out.println();
            System.out.println();
        } finally {
            a.ansiOff();
        }

    }


    //////

    public static String makeColorTable(Ansifer a) {
        return makeColorTable(a, true) +
                "\n" +
                makeColorTable(a, false) +
                "\n" +
                makeStyleTable(a);
    }

    public static String makeColorTable(Ansifer a, boolean first) {
        StringBuilder b = new StringBuilder();
        for (AnsiferColor color : AnsiferColor.values()) {
            b.append("   ");
            for (AnsiferColor background : AnsiferColor.values()) {
                if (background.bright()) {
                    continue;
                }
                if ((background.code() > 3) == first) {
                    continue;
                }
                String s = " " + color.name() + "/" + background.name() + " ";

                b.append(a.color(a.getStyle(color.toString(), background.toString()), String.format("%-19s", s)));
                b.append(" ");
            }
            b.append("\n");
        }
        return b.toString();
    }

    public static String makeStyleTable(Ansifer a) {
        List<String> lstNames = new ArrayList<>(a.getStyleNames());
        lstNames.sort(null);
        int maxLen = 0;
        for (String nm : lstNames) {
            maxLen = Math.max(maxLen, nm.length());
        }
        StringBuilder b = new StringBuilder();
        for (String nm : lstNames) {
            b.append("   ");
            String s = " " + nm + " ";

            b.append(a.color(a.getStyle(nm), String.format("%-" + maxLen + "s", s)));
            b.append(" ");
            b.append("\n");
        }
        return b.toString();
    }


}
