package jandcode.jc.impl

import jandcode.commons.*
import jandcode.commons.ansifer.*
import jandcode.jc.*

/**
 * Печать map в удобном виде, с подсветкой.
 * Для вывода информации в jc
 */
class PrintMap {

    Ctx ctx

    /**
     * Шаг отступа для вложенных структур
     */
    int indentStep = 4

    PrintMap(Ctx ctx) {
        this.ctx = ctx
    }

    public void printMap(Map map) {
        printMap(map, 0)
    }

    public void printMap(Map map, int indent) {
        int mx = 0
        for (a in map) {
            def len = UtString.toString(a.key).length()
            if (len > mx) {
                mx = len
            }
        }
        int styleNum = ((int) (indent / indentStep)) % 2 + 1
        String style = "c" + styleNum
        //
        String idns = "  " + UtString.repeat(" ", indent)
        for (a in map) {
            String k = idns + UtString.padRight(UtString.toString(a.key), mx)
            print(Ansifer.getInst().style(style, k));
            print(" = ");
            if (a.value instanceof Collection) {
                println("")
                for (v in a.value) {
                    String s = idns + UtString.padRight(UtString.toString(""), mx) + " | " + v
                    println(s)
                }
            } else if (a.value instanceof Map) {
                println()
                printMap(a.value, indent + indentStep)
            } else {
                println(UtCnv.toString(a.value))
            }
        }
        //
    }


}
