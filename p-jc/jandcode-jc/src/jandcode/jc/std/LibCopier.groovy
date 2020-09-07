package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*

/**
 * Копировальщик библиотек.
 * Используется как утилита.
 */
class LibCopier extends ProjectScript {

    class IncludeLib {
        String name
        boolean withDepens
    }

    private List<IncludeLib> itemsInclude = []
    private List<String> itemsExclude = []
    private ListLib items

    /**
     * Подкаталог для исходников
     */
    String srcDir = "src"

    /**
     * Имя для lib.xml файла
     */
    String libXmlName = "index"

    /**
     * Создавать ли index.lib.xml
     */
    boolean createLibXml = true

    /**
     * Добавить библиотеку.
     * @param names может быть списком или объектом. Если объект, тогда:
     * строка - имя библиотеки, INamed - getName() имя библиотеки.
     * @param withDepends true - добавлять вместе с зависимостями, false - только
     * указанные библиотеки
     */
    public void add(Object names, boolean withDepends = true) {
        items = null
        List<String> libNames = UtCnv.toNameList(names);
        for (n in libNames) {
            itemsInclude.add(new IncludeLib(name: n, withDepens: withDepends))
        }
    }

    /**
     * Удалить библиотеку.
     * names может быть списком или объектом. Если объект, тогда:
     * строка - имя библиотеки, INamed - getName() имя библиотеки.
     */
    public void remove(Object names) {
        items = null
        List<String> libNames = UtCnv.toNameList(names);
        itemsExclude.addAll(libNames)
    }

    /**
     * Список библиотек в сборнике
     */
    ListLib getItems() {
        if (items == null) {
            items = new ListLib()
            for (it in itemsInclude) {
                if (it.withDepens) {
                    def tmp = ctx.getLibs(it.name)
                    items.addAll(tmp)
                } else {
                    def tmp = ctx.getLib(it.name)
                    items.add(tmp)
                }
            }
            for (a in itemsExclude) {
                items.remove(a)
            }
            items.sort()
        }
        return items
    }

    private boolean validate(Lib a, Object c) {
        if (c == null) return true
        if (c instanceof Boolean) return c
        if (c instanceof Closure) return c(a)
        return false
    }

    /**
     * Скопировать в указанный каталог
     * @param destdir куда
     * @param copyJar копировать ли jar.
     *                Может быть Closure или boolean.
     * @param copySrc копировать ли исходники.
     *                Может быть Closure или boolean.
     */
    void copyTo(String destdir, Object copyJar, Object copySrc) {
        SimXml xx = new SimXmlNode()
        for (a in getItems()) {

            String jarFile = a.name + ".jar"
            String srcFile = UtFile.join(srcDir, a.name + "-src.zip")

            SimXml x = xx.addChild("lib")
            //
            x["name"] = a.name
            x["version"] = a.version
            x["groupId"] = a.groupId
            //
            if (validate(a, copyJar)) {
                ant.copy(file: a.jar, tofile: UtFile.join(destdir, jarFile),
                        preservelastmodified: true)
            }
            if (a.src != "") {
                if (validate(a, copySrc)) {
                    ant.copy(file: a.src, tofile: UtFile.join(destdir, srcFile),
                            preservelastmodified: true)
                }
            }
            //
            for (LibDependsGroup g : a.depends.groups) {
                if (g.isEmpty()) {
                    continue
                }
                def sd = UtString.join(g.libs.names, ",")
                x["depends." + g.name] = sd
            }

        }
        if (createLibXml) {
            xx.save().toFile(destdir + "/" + libXmlName + ".lib.xml")
        }
    }

    /**
     * Скопировать jar в указанный каталог
     * @param destdir куда
     */
    void copyTo(String destdir) {
        copyTo(destdir, true, false)
    }

}
