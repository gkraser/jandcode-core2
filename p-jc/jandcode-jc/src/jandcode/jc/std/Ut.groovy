package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.named.*
import jandcode.commons.process.*
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
     * Возвращает вывод консоли, если saveout=true.
     * Иначе возвращает пустой список.
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
     *
     * @param env : Map
     *    Переменные среды
     */
    public List<String> runcmd(Map pp) {
        def p = asVariantMap(pp)
        //
        RunCmd runCmd = new RunCmd()
        runCmd.setDir(p.getString('dir', project.wd.path))
        runCmd.setShowout(p.getBoolean('showout', true))
        runCmd.setSaveout(p.getBoolean('saveout', false))
        if (p.cmd instanceof List) {
            runCmd.setCmd((List) p.cmd)
        } else {
            runCmd.setCmd(p.getString("cmd"))
        }

        boolean err = p.getBoolean('err', true)

        if (p.containsKey('env')) {
            runCmd.getEnv().putAll((Map) p.get('env'))
        }

        log.debug "runcmd: [" + runCmd.getCmd().join(' ') + "] in [${runCmd.dir}]"

        runCmd.run()

        if (err) {
            if (runCmd.exitCode > 0) {
                def c = runCmd.params.join(' ')
                c = "Ошибка при выполнении [${c}]"
                if (runCmd.saveout) {
                    def r = runCmd.out.join('\n')
                    if (r != "") {
                        c = c + "\n[console]>>>\n${r}\n<<<"
                    }
                }
                throw new XError(c)
            }
        }

        return runCmd.out
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
            if (UtFile.isWindows()) {
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
     * Если файл scriptFileName существует, то он включается в проект
     *
     * @param scriptFileName имя файла скрипта
     */
    void includeIfExists(String scriptFileName) {
        String f = wd(scriptFileName)
        if (UtFile.exists(f)) {
            project.include(f)
        }
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

    ////// vars

    /**
     * Возвращает значение из vars проекта с учетом значения в корневом
     * проекте. Если переменная определена в корневом проекте,
     * возвращается она. Если нет, то из текущего проекта.
     * Если там и там не определена, возвращается значение по умолчанию.
     * @param key ключ переменной
     * @param defaultValue значение по умолчанию
     */
    Object getVar(String key, Object defaultValue) {
        Object v

        // своя
        v = getProject().getVars().get(key)
        if (v != null) {
            return v
        }

        // из root project
        Project rp = getRootProject()
        if (rp != null) {
            v = rp.getVars().get(key)
            if (v != null) {
                return v
            }
        }

        // по умолчанию
        return defaultValue
    }

    /**
     * Установить значение переменной проекта
     * @param key ключ переменной
     * @param value значение переменной. При значении null перемнная удаляется
     */
    void setVar(String key, Object value) {
        if (value == null) {
            getProject().getVars().remove(key)
        } else {
            getProject().getVars().put(key, value)
        }
    }

}
