package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.named.*
import jandcode.commons.stopwatch.*
import jandcode.commons.variant.*
import jandcode.jc.*
import jandcode.jc.impl.*

/**
 * Стандартные утилиты для скриптов проекта.
 */
class Ut extends ProjectScript {

    Ut(Project project) {
        setProject(project)
    }

    ////// timer

    private StopwatchSet _stopwatch = new DefaultStopwatchSet(true) {
        void print(Stopwatch sw) {
            log(sw.toString())
        }
    }

    /**
     * Секундомеры
     */
    StopwatchSet getStopwatch() {
        return _stopwatch
    }

    ////// clean

    /**
     * Очистка каталога. После выполнения команды имеем существующий пустой каталог
     * @param dir Какой каталог чистить
     * @param excludes Маски исключаемых из удаления файлов
     */
    public void cleandir(String dir, List excludes = null) {
        if (UtString.empty(dir)) {
            error("Не указан каталог для очистки")
        }
        log.debug "Clean dir [${dir}]"
        ant.mkdir(dir: dir);
        ant.delete(includeemptydirs: true) {
            fileset(dir: dir, defaultexcludes: false) {
                include(name: "**/*")
                if (excludes != null) {
                    for (ex in excludes) {
                        exclude(name: ex)
                    }
                }
            }
        }
    }

    /**
     * Очистка файла. После выполнения команды имеем существующий пустой каталог,
     * в котором должен лежать файл и файл не существует
     * @param file Какой файл чистить
     */
    public void cleanfile(String file) {
        if (UtString.empty(file)) {
            error("Не указан файл для очистки")
        }
        log.debug "Clean file [${file}]"
        ant.mkdir(dir: UtFile.path(file))
        ant.delete(file: file)
    }

    //////  convertors

    /**
     * Возвращает IVariantMap для указанного Map.
     * Если m==null, возвращается пустой Map.
     * Используется для анализа параметров в методах, которые принимают параметры как map.
     */
    public IVariantMap asVariantMap(Map m) {
        if (m == null) {
            m = new LinkedHashMap();
        }
        if (m instanceof IVariantMap) {
            return m as IVariantMap;
        }
        return new VariantMapWrap(m);
    }

    ////// run

    /**
     * Запуск внешнего приложения как команды ОС, без использования ant.
     * В windows запуск делается через cmd.exe.
     *
     * @param cmd
     *    Строка с параметрами, разделенная пробелами или список параметров.
     *    Например cmd:['dir', '/s'] или cmd:'dir /s'
     *
     * @param dir :String
     *    Рабочий каталог запуска. Если не указан, используется рабочий каталог проекта
     *
     * @param charset :String
     *    Кодировка консоли. По умолчанию совпадает с той, в которой запущена jc
     *
     * @param showout :boolean
     *    Показывать вывод на консоль. По умолчанию true
     *
     * @param saveout : boolean
     *    Запоминать вывод на консоль. В этом случае команда возвращает массив строк с выводом.
     *    По умолчанию false.
     *
     * @param err : boolean
     *    При true (по умолчанию) генерить ошибку если код возврата не 0
     */
    public List<String> runcmd(Map pp) {
        def p = asVariantMap(pp)
        //
        List params = []
        if (UtFile.isWindows()) {
            params.add('cmd.exe')
            params.add('/C')
        }
        String dir = p.get('dir', project.wd.path)
        String charset = p.get('charset', UtConsole.getConsoleCharset())
        def showout = p.get('showout', true)
        def saveout = p.get('saveout', false)
        def err = p.get('err', true)
        //
        if (p.cmd instanceof List) {
            params.addAll((List) p.cmd)
        } else {
            String s = p.getString("cmd", "")
            def lp = s.split(" ")
            for (a in lp) {
                params.add(a)
            }
        }
        //
        if (params.size() == 0) {
            error UtLang.t("Параметр cmd не задан")
        }

        log.debug "runcmd: [" + params.join(' ') + "] in [${dir}]"

        //
        List<String> result = []

        ProcessBuilder pb = new ProcessBuilder(params)
        pb.directory(new File(dir))

        Process pr

        if (showout && !saveout) {
            // особый случай, как в обычной консоли
            pb.inheritIO()
            pr = pb.start()

        } else {
            pb.redirectErrorStream(true);
            pr = pb.start();
            BufferedReader inr = new BufferedReader(new InputStreamReader(pr.getInputStream(), charset));
            String line = inr.readLine();
            while (line != null) {
                if (showout) {
                    System.out.println(line)
                }
                if (saveout) {
                    result.add(line)
                }
                line = inr.readLine();
            }
        }

        pr.waitFor()
        def rescode = pr.exitValue()
        if (err) {
            if (rescode > 0) {
                def c = params.join(' ')
                c = "Ошибка при выполнении [${c}]"
                if (saveout) {
                    def r = result.join('\n')
                    if (r != "") {
                        c = c + "\n[console]>>>\n${r}\n<<<"
                    }
                }
                error c;
            }
        }

        return result
    }

    /**
     * Найти полный путь до указанного приложения
     * @param cmd приложение
     * @param err true - генерить ошибку, если не найдено
     * @param warn true - генерить сообщение влоге warn, если не найдено и вернуть
     *             пустую строку, если не найдено
     * @return полный путь до найденного приложения
     */
    String findcmd(Map pp) {
        def p = asVariantMap(pp)
        String cmd = p.getString("cmd")
        boolean warn = p.getBoolean("warn", false)
        boolean err = p.getBoolean("err", false)
        if (warn) {
            err = true
        }
        try {
            List<String> res = []
            if (UtFile.isWindows()){
                res = ut.runcmd(cmd: "where ${cmd}", saveout: true, showout: false, err: true)
            } else {
                res = ut.runcmd(cmd: "which ${cmd}", saveout: true, showout: false, err: true)
            }
            if (res.size() > 0) {
                return res[0]
            } else {
                return ""
            }
        } catch (Exception e) {
            if (err) {
                throw e
            } else if (warn) {
                log.warn(e)
            }
            return ""
        }
    }

    ////// prints

    public String makeDelim(String text = "") {
        return UtString.delim(text, "-", JcConsts.DELIM_LEN)
    }

    /**
     * Рисует разделитель в log
     *
     * @param text
     *    Текст в заголовке разделителя
     */
    public void delim(String text = "") {
        log.info(makeDelim(text))
    }

    /**
     * Напечатать map в удобочитаемом виде
     */
    public void printMap(Map map) {
        new PrintMap(ctx).printMap(map)
    }

    /**
     * Напечатать classpath
     * @param cp или строка со списком путей или список библиотек или путей
     */
    public String makePrintClasspath(Object cp, String header = "Using classpath") {
        if (cp == null) {
            return ""
        }
        if (cp instanceof CharSequence) {
            cp = UtCnv.toList(cp)
        }
        for (it in cp) {
            if (it instanceof Lib) {
                it.classpath  // трогаем для компиляции или resolve
            }
        }
        StringBuilder sb = new StringBuilder()
        //
        if (!UtString.empty(header)) {
            if (!header.endsWith(":")) {
                header += ":"
            }
            sb.append(header).append("\n")
        }
        for (it in cp) {
            def z = it
            if (z instanceof Lib) {
                z = z.classpath
            }
            if (UtString.empty(z)) {
                continue
            }
            sb.append("      | ${z}").append("\n")
        }
        return sb.toString().trim()
    }

    //////  dev

    /**
     * Ищется файл scriptFileName начиная с каталога проекта и вверх по иерархии каталогов.
     * Первый найденный - включается в проект.
     *
     * Используется для настройки среды конкретного проекта конкретным разработчиком.
     *
     * @param scriptFileName имя файла скрипта
     */
    void includeDev(String scriptFileName) {
        String f = UtFile.findFileUp(scriptFileName, wd())
        if (f == null) {
            return
        }
        project.include(f)
    }

    /**
     * Если проект RootProject, то давляет в его dev-зависимости все библиотеки в
     * исходниках.
     * Это позволяет иметь в IDE все модули в исходниках, даже если проект явно
     * от них не зависит, что удобно при массовом рефакторинге.
     */
    void addDependsAllSource() {
        RootProject rp = getIncluded(RootProject)
        if (rp == null) {
            return
        }
        for (lib in ctx.libs) {
            if (lib.sourceProject != null) {
                rp.depends.dev.add(lib.name)
            }
        }
    }

    /**
     * Получить имя-info.
     * Используется для отображения имени в различных логгерах.
     * Возможно в будущем станет особым плагином.
     *
     * @param obj объект (проект например)
     * @return имя
     */
    String nameInfo(Object obj) {
        if (obj == null) {
            return null

        } else if (obj instanceof Project) {
            Project p = obj
            String s = p.getName()
            if (p.getIncluded(RootProject) != null) {
                s = s + "-(root)"
            }
            return s

        } else if (obj instanceof INamed) {
            return ((INamed) obj).getName()

        }
        return obj.toString()
    }

}
