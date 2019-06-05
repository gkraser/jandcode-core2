package jandcode.jc.impl

import jandcode.commons.*
import jandcode.commons.ansifer4.*
import jandcode.jc.*

/**
 * Печать списка библиотек
 */
class PrintLibs {

    Ctx ctx
    Ansifer ansi

    PrintLibs(Ctx ctx) {
        this.ctx = ctx
        ansi = UtAnsifer.getAnsifer()
    }

    /**
     * Напечатать список библиотек
     *
     * @param mode :
     *      'short' - именя и версия
     *      'array' - "'NAME'," - для включения в список
     *      по умолчанию - полная информация
     */
    void printLibs(List lst, String mode, boolean sort) {
        if (lst == null) {
            return
        }
        List<Lib> tmp = []
        tmp.addAll(lst);
        if (sort) {
            tmp.sort { a, b -> a.name <=> b.name }
        }

        boolean onlyNames = (mode == 'short' || mode == 'array')


        for (Lib z : tmp) {
            if (mode != 'array') {
                print(ansi.color("c1", z.getName()));
                print(" ");
                println(ansi.color("c2", z.getVersion()));
            } else {
                println("'${z.getName()}',");
            }

            if (onlyNames) {
                continue
            }

            printInfo("groupId", z.groupId);
            if (z.artifactId != z.name) {
                printInfo("artifactId", z.artifactId);
            }

            Project sp = z.getSourceProject()
            String cp = null
            String sr = null
            String jr = null;

            if (sp == null) {
                cp = z.getClasspath();
                sr = z.getSrc();
                if (!UtString.empty(cp)) {
                    if (UtFile.isFile(cp)) {
                        jr = cp;
                        cp = null;
                    }
                }
            }

            if (!UtString.empty(cp)) {
                printInfo("classpath", cp);
            }
            if (!UtString.empty(jr)) {
                printInfo("jar", jr);
            }
            if (!UtString.empty(sr)) {
                printInfo("src", sr);
            }
            if (sp != null) {
                printInfo("project", sp.getProjectFile());
            }

            List<String> mods = z.getModules()
            if (mods.size() > 0) {
                printInfo("modules", UtString.join(mods, ","));
            }

            for (LibDependsGroup g : z.getDepends().groups) {
                if (!g.isEmpty()) {
                    printInfo("deps." + g.name, UtString.join(g.libs.names, ","));
                }
            }

            println("")
        }

    }

    void printInfo(String title, String txt) {
        title = UtString.padLeft(title + ":", 12) + " "
        print(title)
        println(ansi.color("c-gray", txt));
    }

}
