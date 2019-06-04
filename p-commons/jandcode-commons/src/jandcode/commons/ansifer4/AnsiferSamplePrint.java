package jandcode.commons.ansifer4;

import jandcode.commons.*;

/**
 * Печать примеров на консоле
 */
public class AnsiferSamplePrint {

    public static String makeColorTable(Ansifer a) {
        StringBuilder b = new StringBuilder();
        for (AnsiferColor color : AnsiferColor.values()) {
            b.append("   ");
            for (AnsiferColor background : AnsiferColor.values()) {
                if (background.bright()) {
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


    /**
     * Печать всех цветов
     */
    public static void printAllColors() {

        Ansifer a = UtAnsifer.getAnsifer();
        a.install();

        ////////

        System.out.println();
        System.out.println();
        System.out.println(makeColorTable(a));
        System.out.println();
        System.out.println();

    }

    public static void main(String[] args) {
        printAllColors();
    }

}
