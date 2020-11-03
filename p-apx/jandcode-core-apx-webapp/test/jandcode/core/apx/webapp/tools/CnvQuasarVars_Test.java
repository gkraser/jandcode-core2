package jandcode.core.apx.webapp.tools;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

public class CnvQuasarVars_Test extends Utils_Test {

    /**
     * Это примитивный преобразователь файла с переменными sass
     * в переменные less. Собран на коленках, заточен под quasar.
     * Вроде не нужен пока, но пусть тут останется на всякий случай и для истории...
     */
    class Cnv {

        String ignorePfx = "//IGNORE// ";
        Map<String, String> varsInfo = new HashMap<>();

        public Cnv() {
            varsInfo.put("@h-tags", "ignore");
            varsInfo.put("@spaces", "list");
            varsInfo.put("@flex-gutter", "list");
            varsInfo.put("@headings", "list");
        }

        public String convert(String sass) {
            StringBuilder sb = new StringBuilder();
            sb.append("//@formatter:off\n");
            String[] strs = sass.split("\n");

            for (String s : strs) {

                if (s.startsWith("$")) {
                    sb.append(cnvStr(s)).append("\n");
                } else {
                    sb.append(s).append("\n");
                }
            }

            sb.append("//@formatter:on\n");
            return sb.toString();

        }

        public String cnvStr(String orig) {


            String src = orig;

            src = src.replaceAll("!default", "").trim();
            src = src.replaceAll("\\$", "@");

            int a = src.indexOf(':');
            if (a == -1) {
                // не переменная!
                src = ignorePfx + src;
            } else {

                String name = src.substring(0, a).trim();
                String value = src.substring(a + 1).trim();

                String vi = varsInfo.get(name);
                if ("ignore".equals(vi)) {
                    // эту игнорируем
                    return ignorePfx + src;
                }

                System.out.println("VAR=>" + name);

                boolean hasZpt = value.indexOf(',') != -1;
                boolean hasSemi = value.indexOf(':') != -1;

                if (hasZpt && hasSemi && value.startsWith("(") && value.endsWith(")")) {
                    // есть : и , - это map...

                    String newStr = "";
                    String vals = "";
                    String s = value.substring(1, value.length() - 1);
                    String[] as = s.split(",");
                    for (String s1 : as) {
                        String[] nv = s1.split(":");
                        String nvName = nv[0].trim();
                        String nvValue = nv[1].trim();

                        if ("list".equals(vi)) {
                            if (nvName.startsWith("'") || nvName.startsWith("\"")) {
                                nvName = nvName.substring(1, nvName.length() - 1);
                            }
                            if (newStr.length() != 0) {
                                newStr = newStr + ", ";
                            }
                            newStr = newStr + nvName;
                        } else {
                            if (nvName.startsWith("'") || nvName.startsWith("\"")) {
                                nvName = nvName.substring(1, nvName.length() - 1);
                            }
                            newStr = newStr + name + "--" + nvName + ": " + nvValue + ";\n";
                            if (vals.length() != 0) {
                                vals = vals + ", ";
                            }
                            vals = vals + nvName;

                        }
                    }

                    if ("list".equals(vi)) {
                        newStr = name + ": " + newStr + ";";
                    } else {
                        newStr = newStr + "\n" + name + ": " + vals + ";";
                    }

                    //src = ignorePfx +"ZZZ "+ s;
                    src = newStr;
                } else if (hasZpt && !hasSemi && value.startsWith("(") && value.endsWith(")")) {
                    // есть , и нет :, список переменных скорее всего
                    String newStr = "";
                    String s = value.substring(1, value.length() - 1);
                    String[] as = s.split(",");
                    for (String s1 : as) {
                        String nvValue = s1.trim();

                        if (newStr.length() != 0) {
                            newStr = newStr + ", ";
                        }
                        if (nvValue.startsWith("@")) {
                            nvValue = nvValue.substring(1);
                        }
                        newStr = newStr + nvValue;
                    }

                    src = name + ": " + newStr + ";";

                } else {
                    // скорее всего обычная переменная
                    src = src + ";";
                }

            }
            return src;
        }


    }

    @Test
    public void test1() throws Exception {
        Cnv z = new Cnv();
        String sass = UtFile.loadString(utils.getTestFile("variables.sass"));
        String less = z.convert(sass);

        System.out.println(less);

        String outPath = "temp";

        String outFile = UtFile.join(outPath, "_q_vars.less");
        UtFile.saveString(less, new File(outFile));

    }


}
