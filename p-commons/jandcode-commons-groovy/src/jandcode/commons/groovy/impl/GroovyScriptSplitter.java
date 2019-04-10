package jandcode.commons.groovy.impl;

import jandcode.commons.*;

import java.util.regex.*;

/**
 * Простой препроцессор для скрипта groovy.
 * Выделяет из скрипта 2 части: область импорта и тело скрипта
 */
public class GroovyScriptSplitter {

    private String importPart = "";
    private String bodyPart = "";

    public GroovyScriptSplitter(String scriptText) {
        handle(scriptText);
    }

    //////

    public String getImportPart() {
        return importPart;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    /**
     * Количество строк в import-части
     */
    public int getImportCountLines() {
        if (importPart.length() == 0) {
            return 0;
        }
        int n = 0;
        int a = -1;
        while (true) {
            n++;
            a = importPart.indexOf('\n', a + 1);
            if (a == -1) {
                break;
            }
        }
        return n;
    }

    //////

    private void handle(String scriptText) {
        if (UtString.empty(scriptText)) {
            return;
        }
        Pattern z = Pattern.compile("^\\s*(import.*+)", Pattern.MULTILINE);
        Matcher m = z.matcher(scriptText);
        int posEndImport = 0;

        while (m.find()) {
            posEndImport = m.end(1);
        }

        if (posEndImport > 0) {
            importPart = scriptText.substring(0, posEndImport);
            bodyPart = scriptText.substring(posEndImport);
        } else {
            bodyPart = scriptText;
        }
    }

}
