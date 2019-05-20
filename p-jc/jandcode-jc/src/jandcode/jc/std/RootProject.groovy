package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.impl.depends.*
import jandcode.jc.std.idea.*

/**
 * Корневой проект для набора java-модулей.
 * Сам java-модулем не является.
 * Может содержать и не JavaProject
 */
class RootProject extends ProjectScript implements ILibDepends, ILibDependsGrab {

    /**
     * Событие: генерация ipr закончена. Желающие модули могут его модифицировать.
     * На это событие подписываются модули, которые желают что то добавить
     * специфичное для себя в корневом проекте, например конфигурацию запуска.
     */
    static class Event_GenIprForModule extends BaseJcEvent {
        /**
         * Генерируемый файл
         */
        IprXml x

        Event_GenIprForModule(IprXml x) {
            this.x = x
        }

    }

    private List _modules = []

    /**
     * Список модулей проекта в правильном порядке (сначала зависимые, потом зависящие).
     * При инициализации проекта - имена каталогов модулей относительно
     * корневого проекта. После инициализации - ссылки на проекты модулей.
     */
    List getModules() {
        return _modules
    }

    void setModules(Collection modules) {
        this._modules.clear()
        this._modules.addAll(modules)
    }

    /**
     * Группы модулей.
     * key - имя группы
     * value - list со списком имен модулей, входящих в эту группу.
     *         Или closure, которая принимает имя модуля и возвращает true, если модуль
     *         входит в эту группу.
     *
     * Используется при генерации проекта idea
     */
    Map moduleGroups = [:]

    /**
     * каталоги, которые исключаются из модуля и не видны в idea.
     */
    List<String> dirsExclude = ["temp", "out", "_product", "_jc", "lib", "_gen"]

    /**
     * Имя группы модулей, в которую будут входить java-модули, если для них
     * явно не установлена группа.
     */
    String moduleGroup = ""

    /**
     * Зависимости
     */
    LibDepends depends

    /**
     * maven: groupId.
     * Если установлен, то автоматически будет назначен всем модулям,
     * если в модуле нет собственного groupId
     */
    String groupId

    /**
     * Включать ли jandcode-jc в зависимости dev по умолчанию.
     * Нужно для полноценного редактирования jc-файлов.
     */
    boolean autoJcDepends = true


    protected void onInclude() throws Exception {
        if (getIncluded(JavaProject) != null) {
            error("Нельзя в одном проекте подключать RootProject и JavaProject")
        }

        this.depends = new LibDependsImpl(ctx, this)

        //
        afterLoad(this.&onAfterLoad)

        // utilites
        include(CreateProject)
        include(GenIdea_RunJc)

        // showinfo
        include(Showinfo)
        onEvent(Showinfo.Event_GetInfo, this.&showinfoHandler)

        // showlibs
        include(Showlibs)

        // idea
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)
        onEvent(GenIdea.Event_GenIml, this.&genImlHandler)

        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

        // clean
        include(CleanProject)
        onEvent(CleanProject.Event_Clean, this.&cleanHandler)

        // build
        include(BuildProject)
        onEvent(BuildProject.Event_Build, this.&buildHandler)

        //
        include(RegistryModuleDef)

        // cm
        cm.add("compile", this.&cmCompile, "Компиляция")
        cm.add("compile-test", this.&cmCompileTest, "Компиляция тестов")
        cm.add("test", this.&cmTest, "Запуск unit-тестов",
                cm.opt("q", false, "Не генерировать ошибки при проваленных тестах")
        )
        cm.add("gen-idea", this.&cmGenIdea, "Генерация файлов idea")
    }

    void onAfterLoad() {
        List tmp = []
        for (mname in modules) {
            Project m
            if (mname instanceof String) {
                // загружаем модуль
                m = load(mname)

            } else if (mname instanceof Project) {
                m = mname

            } else {
                error("В modules можно указывать только строки или проекты")
            }
            tmp.add(m)

            // что бы можно было gen-idea делать
            if (m.impl(ILibDepends).size() == 0) {
                m.include(DummyImlProject)
            }
        }
        modules.clear()
        modules.addAll(tmp)

        // автодобавление jc в зависимости
        if (autoJcDepends) {
            String libJcName = "jandcode-jc"
            Lib libJc = ctx.findLib(libJcName)
            if (libJc != null) {
                depends.dev.add(libJcName)
            }
        }

        // gen-src check
        boolean needGenSrc = false
        for (Project m in modules) {
            if (m.cm.find("gen-src") != null) {
                needGenSrc = true
                break
            }
        }
        if (needGenSrc) {
            cm.add("gen-src", this.&cmGenSrc, "Генерация исходников")
        }

        ClasspathUsed cp = getIncluded(ClasspathUsed)
        if (cp != null) {
            // в процессе загрузки были вызовы classpath, включаем все в dev
            depends.dev.add(cp.usedClasspath)
        }

        /// idea
        GenIdea genIdea = getIncluded(GenIdea)
        if (genIdea != null) {
            if (genIdea.ideaProjectName == project.name) {
                genIdea.ideaProjectName = project.name + "-(project)"
            }
        }
    }

    void grabDepends(LibDepends deps) {
        // зависимости модулей
        for (Project p in modules) {
            for (ILibDependsGrab dg : p.impl(ILibDependsGrab)) {
                dg.grabDepends(deps)
            }
            for (ILibDepends dp : p.impl(ILibDepends)) {
                dp.depends.validate()
                JavaProject jp = p.getIncluded(JavaProject)
                if (jp != null) {
                    deps.dev.add(p.name)
                }
                for (LibDependsGroup g : dp.depends.groups) {
                    deps.dev.add(g.names)
                }
            }
        }
    }

    /**
     * Выполнить команду для каждого модуля, в котором она определена
     * @param cmName имя команды
     * @param args аргументы
     */
    void forModulesExecCm(String cmName, Map args = null) {
        for (Project m in modules) {
            def cm = m.cm.find(cmName)
            if (cm != null) {
                cm.exec(args)
            }
        }
    }

    void cleanHandler() {
        forModulesExecCm('clean')
    }

    void cmCompile() {
        forModulesExecCm('compile')
    }

    void cmCompileTest() {
        forModulesExecCm('compile-test')
    }

    void cmGenSrc() {
        forModulesExecCm('gen-src')
    }

    void cmTest(CmArgs args) {

        // перед test обязательно делаем prepare
        cm.exec("prepare")
        //

        forModulesExecCm('test', [q: true]) // без генерации ошибок
        //
        def execTestFailModules = []  // модули, в которых провалены тесты

        def applyRoot
        applyRoot = { RootProject pr ->
            for (Project m in pr.modules) {
                JUnitProject jm = m.getIncluded(JUnitProject)
                if (jm != null) {
                    if (jm.testExecFail) {
                        execTestFailModules.add(m.name)
                    }
                }
                RootProject rm = m.getIncluded(RootProject)
                if (rm != null) {
                    applyRoot(rm)
                }
            }
        }
        applyRoot(project.getIncluded(RootProject))

        // ошибки при выполнении тестов
        if (execTestFailModules.size() > 0) {
            // если нужно, генерим ошибку
            if (!args.q) {
                error("Run test error in modules: " + UtString.join(execTestFailModules, ","))
            } else {
                log.warn("Run test error in modules: " + UtString.join(execTestFailModules, ","))
            }
        }

    }

    ////// showinfo

    void showinfoHandler(Showinfo.Event_GetInfo e) {
        Map m = e.m
        //
        //
        m['groupId'] = getGroupId()
        m['Modules'] = modules.collect { it.name }
    }

    ////// idea

    void cmGenIdea() {
        def g = include(GenIdea)

        // перед gen-idea обязательно делаем prepare
        cm.exec("prepare")
        //
        g.genIml()
        g.genIpr()
        g.genIws()
    }

    void genImlHandler(GenIdea.Event_GenIml e) {
        ImlXml x = e.x
        //
        x.removeFolders()
        x.addFolders(dirsExclude, "exclude")
        //
        x.removeDepends()
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ListLib rlibs = deps.all.libsAll
        x.addDepends(rlibs)
    }

    private doGenIdeaForProject(Project p) {
        def cmGenIdea = p.cm.find('gen-idea')
        if (cmGenIdea == null) {
            p.include(DummyImlProject)
            cmGenIdea = p.cm.find('gen-idea')
        }
        cmGenIdea.exec()
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x
        //
        x.removeModules()
        x.addModule(project) // себя то же

        // тут будем собирать все модули, которые попали в idea проект
        Set<Project> ideaModules = new LinkedHashSet<>()
        ideaModules.add(project)

        // java модули
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ListLib rlibs = deps.all.libsAll
        for (Lib lib in rlibs) {
            if (lib.sourceProject != null) {
                ideaModules.add(lib.sourceProject)
                // генерим iml
                doGenIdeaForProject(lib.sourceProject)
                //
                SimXml xm = x.addModule(lib.sourceProject)
                // если модуль в какой то группе, добавляем группу для него
                String gn = getModuleGroup(lib.sourceProject.name)
                if (!UtString.empty(gn)) {
                    xm['group'] = gn
                }
            }
        }

        // остальные модули
        for (Project m in modules) {
            if (m.getIncluded(JavaProject) == null) {
                ideaModules.add(m)
                // генерим iml
                doGenIdeaForProject(m)
                //
                SimXml xm = x.addModule(m)
                // если модуль в какой то группе, добавляем группу для него
                String gn = getModuleGroup(m.name)
                if (!UtString.empty(gn)) {
                    xm['group'] = gn
                }
            }
        }

        // для всех модулей генерим событие
        for (Project m : ideaModules) {
            m.fireEvent(new Event_GenIprForModule(x))
        }
    }

    /**
     * Возвращает имя группы для указанного имени модуля или "", если модуль не в группе
     */
    String getModuleGroup(String moduleName) {
        // группа позже, приоритет выше!
        String res = ""
        for (en in moduleGroups) {
            def gCheck = en.value
            String gName = en.key
            if (gCheck instanceof List) {
                for (mn in gCheck) {
                    if (moduleName == mn) {
                        res = gName
                    }
                }
            } else if (gCheck instanceof Closure) {
                if (gCheck(moduleName)) {
                    res = gName
                }
            }
        }
        if (UtString.empty(res)) {
            def lib = ctx.findLib(moduleName)
            if (lib != null && lib.sourceProject != null) {
                def jp = lib.sourceProject.getIncluded(JavaProject)
                if (jp != null) {
                    res = jp.moduleGroup
                }
            }
        }
        return res
    }

    /**
     * Подготовка проекта к использованию
     */
    void prepareHandler() {
        // копируем файлы из каталога data/dev
        copyTemplateFiles(wd("data/dev"), wd())
        // выполняем для модулей prepare, если им это нужно
        forModulesExecCm("prepare")
    }

    /**
     *
     */
    void copyTemplateFiles(String fromDir, String toDir) {
        fromDir = wd(fromDir)
        toDir = wd(toDir)
        if (!UtFile.exists(fromDir)) {
            return
        }
        def scanner = ant.fileset(dir: fromDir) {
            include(name: "**/*")
        }
        for (f in scanner) {
            def tmlFile = f.toString()
            String fn = UtFile.getRelativePath(fromDir, tmlFile)
            String destFile = UtFile.join(toDir, fn)
            if (!UtFile.exists(destFile)) {
                ant.copy(file: tmlFile, tofile: destFile)
            }
        }

    }

    void buildHandler() {
        forModulesExecCm('build')
    }

}
