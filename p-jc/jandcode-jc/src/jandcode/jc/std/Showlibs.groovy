package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.impl.*

/**
 * Просмотр библиотек
 */
class Showlibs extends ProjectScript {

    protected void onInclude() throws Exception {
        cm.add("showlibs", this.&cmShowlibs, "Просмотр библиотек",
                cm.opt("a", false, "Показать все доступные библиотеки"),
                cm.opt("l", "lib", "Показать личные библиотеки из указанного каталога (lib по умолчанию)"),
                cm.opt("d", false, "Показать зависимые библиотеки (по умолчанию)"),
                cm.opt("n", false, "Только имена библиотек (без jar, scr и т.д.)"),
                cm.opt("j", false, "Только имена библиотек в виде для включения в зависимости"),
                cm.opt("g", false, "Скопировать библиотеки и исходники в каталог temp/showlibs"),
                cm.opt("r", false, "Дополнение для опции -g: не включать версию в имя файла"),
                cm.opt("b", false, "Дополнение для опции -g: не копировать библиотеки в исходниках"),
                cm.opt("m", false, "Показать только модули"),
                cm.opt("q", "", "Выбирать только библиотеки, содержащие в имени подстроку"),
                cm.opt("t", false, "Только явно описанные в зависимостях"),
                cm.opt("e", false, "Показать перекрытые библиотеки из числа запрошенных"),
        )
    }

    /**
     * Показать библиотеки проекта
     */
    void cmShowlibs(CmArgs args) {
        Map data = [:]

        if (args.containsKey("l")) {
            def dir = args.l
            if (UtString.empty(dir)) {
                dir = JcConsts.LIB_DIR
            }
            dir = wd(dir)
            data["Dir: ${UtFile.getRelativePath(wd.path, dir)}"] = getLibsDir(dir)
        } else if (args.a) {
            data['All'] = getLibsAll()
        } else if (args.t) {
            makeLibsForProjectSelf(data)
        } else {
            makeLibsForProject(data)
        }

        String mode = ''
        if (args.n) {
            mode = 'short'
        } else if (args.j) {
            mode = 'array'
        }

        if (args.m) {
            // фильтруем модули
            for (key in data.keySet()) {
                List libs = data[key]
                if (libs == null || libs.size() == 0) {
                    continue
                }
                def newLst = []
                for (Lib lib in libs) {
                    if (lib.modules.size() > 0) {
                        newLst.add(lib)
                    }
                }
                data[key] = newLst
            }
        }

        if (args.containsKey("q")) {
            // фильтруем по именам
            String qq = args.getString("q")
            if (qq != "") {
                for (key in data.keySet()) {
                    List libs = data[key]
                    if (libs == null || libs.size() == 0) {
                        continue
                    }
                    def newLst = []
                    for (Lib lib in libs) {
                        if (lib.name.indexOf(qq) != -1) {
                            newLst.add(lib)
                        }
                    }
                    data[key] = newLst
                }
            }
        }

        if (args.e) {
            makeLibsDublicates(data)
        }

        for (en in data) {
            List libs = en.value
            if (libs == null || libs.size() == 0) {
                continue
            }
            //
            println ut.makeDelim(en.key)
            new PrintLibs(ctx).printLibs(libs, mode, !args.e)
        }

        //
        if (args.g) {

            boolean incVersionInFileName = !args.r
            boolean noSourceProject = args.containsKey("b")

            // копировать библиотеки
            String destpath = wd("temp/showlibs")
            println ut.makeDelim("copy libs")
            ut.cleandir(destpath)

            for (en in data) {
                List libs = en.value
                if (libs == null || libs.size() == 0) {
                    continue
                }
                //
                for (Lib lib in libs) {
                    if (lib.isSys()) {
                        // игнорируем системные
                        continue
                    }
                    if (noSourceProject && lib.sourceProject != null) {
                        // игнорируем либы в исходниках
                        continue
                    }
                    String cp = lib.jar
                    if (UtString.empty(cp)) {
                        continue
                    }

                    def outFN = lib.name
                    if (incVersionInFileName) {
                        outFN = outFN + "-" + lib.version
                    }

                    if (UtFile.isFile(cp)) {
                        ant.copy(file: cp, tofile: destpath + "/" + outFN + ".jar",
                                preservelastmodified: true)
                    } else {
                        ant.copy(todir: destpath + "/" + outFN,
                                preservelastmodified: true) {
                            fileset(dir: cp)
                        }
                    }
                    //src
                    String sp = lib.src
                    if (!UtString.empty(sp)) {
                        if (UtFile.isFile(sp)) {
                            ant.copy(file: sp, tofile: destpath + "/src/" + outFN + "-src.zip",
                                    preservelastmodified: true)
                        }
                    }
                }
            }
        }
    }

    //////

    private ListLib getLibsAll() {
        return ctx.getLibs()
    }

    private ListLib getLibsDir(String path) {
        return ctx.loadLibs(wd(path))
    }

    protected void makeLibsForProject(Map data) {
        LibDepends deps = create(LibDependsUtils).getDepends(project)

        ListLib dependsProd = ctx.getLibs(deps.prod.libs, JcConsts.DEPENDS_PROD)
        ListLib dependsDev = ctx.getLibs(deps.all.libs)
        ListLib dependsExdev = deps.exdev.libsAll

        // из dependsDev/Extdev удаляем то, что уже есть в depends. так нагляднее
        for (lib in dependsProd) {
            dependsDev.remove(lib)
            dependsExdev.remove(lib)
        }

        // из dependsDev удаляем то, что уже есть в dependsExdev. так нагляднее
        for (lib in dependsExdev) {
            dependsDev.remove(lib)
        }

        if (dependsDev.size() > 0) {
            data['Depends dev'] = dependsDev
        }
        if (dependsExdev.size() > 0) {
            data['Depends exdev'] = dependsExdev
        }
        data['Depends prod'] = dependsProd
    }

    protected void makeLibsForProjectSelf(Map data) {
        LibDepends deps = create(LibDependsUtils).getDepends(project)

        ListLib dependsProd = deps.prod.libs
        ListLib dependsDev = deps.dev.libs
        ListLib dependsExdev = deps.exdev.libs

        if (dependsDev.size() > 0) {
            data['Depends dev'] = dependsDev
        }
        if (dependsExdev.size() > 0) {
            data['Depends exdev'] = dependsExdev
        }
        if (dependsProd.size() > 0) {
            data['Depends prod'] = dependsProd
        }
    }

    /**
     * Сформировать дубликаты.
     * @param data то, что собираемся выводить. Среди них ищутся дубликаты
     */
    protected void makeLibsDublicates(Map data) {

        // формируем дубликаты
        LibDublicatesHolder holder = new LibDublicatesHolder()
        for (ILibProvider prov : ctx.getLibsProviders()) {
            for (Lib lib : prov.getLibs()) {
                holder.addLib(lib)
            }
        }

        Map newData = [:]

        for (en in data) {
            List libs = en.value
            if (libs == null || libs.size() == 0) {
                continue
            }
            for (Lib lib : libs) {
                def libDef = holder.find(lib.name)
                if (libDef != null && libDef.libs.size() > 1) {
                    String key = lib.name
                    if (libDef.versions.size() > 1) {
                        key += ' : ' + UtString.join(libDef.versions, ',')
                    }
                    newData[key] = libDef.libs
                }
            }
        }

        data.clear()
        data.putAll(newData)
    }

    ////// duplicates

    class LibDublicatesHolder {

        Map<String, LibDef> items = [:]

        class LibDef {
            String name
            List libs = []
            Set versions = new LinkedHashSet()
        }

        void addLib(Lib lib) {
            LibDef libDef = items.get(lib.name)
            if (libDef == null) {
                libDef = new LibDef()
                libDef.name = lib.name
                items.put(lib.name, libDef)
            }
            libDef.libs.add(lib)
            libDef.versions.add(lib.version)
        }

        LibDef find(String name) {
            return items.get(name)
        }
    }

}
