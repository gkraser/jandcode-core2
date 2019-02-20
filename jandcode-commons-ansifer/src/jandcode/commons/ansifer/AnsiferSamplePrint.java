package jandcode.commons.ansifer;

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

                b.append(a.color(color, background, String.format("%-19s", s)));
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

        Ansifer a = Ansifer.getInst();
        a.install();

        ////////

        System.out.println();
        System.out.println();
        System.out.println(makeColorTable(Ansifer.getInst()));
        System.out.println();
        System.out.println();

    }


}
