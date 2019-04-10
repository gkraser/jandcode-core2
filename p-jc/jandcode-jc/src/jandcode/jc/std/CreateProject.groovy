package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Создание проекта. Команда.
 */
class CreateProject extends ProjectScript {

    /**
     * Файл проекта-шаблона
     */
    private String projecttmlScript = Projecttml.PROJECTTML_FILE

    /**
     * Виртуальный каталог в данных jc, каждый подкаталог которого рассматривается как
     * проект-шаблон
     */
    String projecttmlRes = "projecttml"


    protected void onInclude() throws Exception {
        //
        String hlp = UtString.normalizeIndent("""
            Создать проект или файлы по шаблону

            Каждый из шаблонов может требовать дополнительные опции
            командной строки. Для просмотра всех доступных опций для шаблона
            необходимо указать опции '-t:шаблон' и '-h'.
        """)
        cm.add("create", this.&cmCreateProject, hlp,
                cm.opt("t", "", "Имя шаблона проекта или каталог с шаблоном"),
                cm.opt("o", "", "Имя каталога для генерации"),
                cm.opt("l", "Показать список доступных имен шаблонов"),
                cm.opt("ll", "Показать список доступных имен шаблонов с дополнительной информацией"),
                cm.opt("template-options") { Map args ->
                    Projecttml t = getProjecttml(args)
                    if (t != null) {
                        return t.opts
                    } else {
                        return null
                    }
                }
        )
    }

    /**
     * По аргументам cli берет шаблон проекта
     */
    protected Projecttml getProjecttml(Map args) {
        if (args.containsKey("t")) {
            String pn = args.get("t")
            if (UtString.empty(pn)) {
                error("Не указан шаблон в опции -t")
            }
            return getProjecttml(pn)
        }
        return null
    }

    /**
     * По имени шаблона берет шаблон
     */
    protected Projecttml getProjecttml(String path) {
        String path1 = ctx.service(JcDataService).findFile(projecttmlRes + "/" + path)
        if (path1 == null) {
            path1 = UtFile.abs(path)
        }
        String pf = UtFile.join(path1, projecttmlScript)
        if (!UtFile.exists(pf)) {
            error("Не найден шаблон проекта [${path}]")
        }
        Project templateProject = load(pf)
        Projecttml t = templateProject.getIncluded(Projecttml)
        if (t == null) {
            error("Проект ${templateProject.projectFile} не является шаблоном projecttml: отсутствует include(Projecttml)")
        }
        return t
    }

    /**
     * Обработка команды
     */
    void cmCreateProject(CmArgs args) {
        //
        if (args.get("l", false)) {
            showTemplates(false)
            return
        }
        if (args.get("ll", false)) {
            showTemplates(true)
            return
        }

        // tml
        if (!args.containsKey("t")) error("Шаблон не указан")
        Projecttml tml = getProjecttml(args)
        tml.args = args

        log.info("Создание проекта по шаблону [${tml.name}]")

        // хочет ли шаблон сам генерится?
        boolean selfGen = tml.gen != null

        // outdir
        String outdir = args.getString("o")
        if (selfGen) {
            // если генерит сам, то и о каталоге пусть сам заботится
            if (UtString.empty(outdir)) {
                outdir = UtFile.getWorkdir()
            }
            outdir = UtFile.abs(outdir)
        } else {
            if (UtString.empty(outdir)) error("Каталог для генерации не указан")
            outdir = UtFile.abs(wd(outdir))
            if (UtFile.exists(outdir)) error("Каталог [${outdir}] уже существует")
        }
        tml.outdir = outdir

        // инициализация
        if (selfGen) {
            // если генерит сам, то пусть сам все и инитит
        } else {
            ut.cleandir(outdir)
            if (tml.init != null) {
                tml.init()
            }
        }

        // генерация
        if (selfGen) {
            // сам все генерит, пусть сам и разбирается
            tml.gen()
        } else {
            doGen(tml)

            // done
            Projecttml newTml = getProjecttml(outdir)
            if (newTml.done != null) {
                log.info("Настройка созданного проекта...")
                newTml.done()
            }
            newTml.ant.delete(file: projecttmlScript)

            //
            log "Проект создан: ${outdir}"
        }
    }

    protected void showTemplates(boolean fullInfo) {
        Map res = [:]
        List lst = ctx.service(JcDataService).findFiles(projecttmlRes)
        for (f in lst) {
            String nm = UtFile.filename(f)
            String desc
            String path = "unknown"
            try {
                Projecttml t = getProjecttml(f)
                desc = t.desc
                path = t.wd("")
            } catch (e) {
                desc = UtError.createErrorInfo(e).getText()
            }
            if (fullInfo) {
                res[nm] = ["desc": desc, "path": path]
            } else {
                res[nm] = desc
            }
        }
        ut.printMap(res)
    }

    /**
     * Замена текста в строке
     * @param s исходная строка
     * @param replaces key:что, value:на что
     * @return
     */
    protected String repl(String s, Map replaces) {
        for (a in replaces) {
            s = s.replace(a.key, a.value)
        }
        return s
    }

    /**
     * Процесс генерации
     */
    protected void doGen(Projecttml tml) {

        //
        String srcdir = tml.wd.path.replace("\\", "/") + "/"
        String outdir = tml.outdir.replace("\\", "/") + "/"
        //

        // пустые каталоги
        def scannerED = ant.dirset(dir: srcdir) {
            include(name: "**/*")
            for (ex in tml.excludes) {
                exclude(name: ex)
            }
        }
        for (ff in scannerED) {
            def fd = new File(ff.toString())
            if (fd.list().length != 0) continue
            //
            def f = ff.toString().replace("\\", "/")
            def sn = UtString.removePrefix(f, srcdir)
            if (sn == null) continue
            sn = repl(sn, tml.replaces)
            def outf = outdir + sn
            ant.mkdir(dir: outf)
        }

        // файлы
        def scanner = ant.fileset(dir: srcdir, defaultexcludes: false) {
            include(name: "**/*")
            for (ex in tml.excludes) {
                exclude(name: ex)
            }
        }
        for (ff in scanner) {
            def f = ff.toString().replace("\\", "/")
            def sn = UtString.removePrefix(f, srcdir)
            if (sn == null) continue
            sn = repl(sn, tml.replaces)
            def outf = outdir + sn
            //
            if (tml.textfiles.contains(UtFile.ext(f))) {
                log.debug "write with replace: ${sn}"
                def s = UtFile.loadString(f)
                s = repl(s, tml.replaces)
                ut.cleanfile(outf)
                UtFile.saveString(s, new File(outf))
            } else {
                ant.copy(file: f, tofile: outf)
            }
        }

    }

}
