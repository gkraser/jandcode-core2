package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.moduledef.*
import jandcode.commons.moduledef.impl.*
import jandcode.commons.named.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.impl.depends.*
import jandcode.jc.impl.lib.*
import jandcode.jc.std.idea.*

/**
 * java-проект
 */
class JavaProject extends ProjectScript implements ILibDepends {

    public static class Event_BeforeCompile extends BaseJcEvent {
    }

    public static class Event_AfterCompile extends BaseJcEvent {
    }

    public static class Event_BeforeCompileTest extends BaseJcEvent {
    }

    public static class Event_AfterCompileTest extends BaseJcEvent {
    }

    /**
     * Задача генерации исходников
     */
    public class GenTask {

        /**
         * В какой каталог генерировать. Абсолютный путь
         */
        String dir

        /**
         * В какой каталог генерировать с учетом packageRoot. Если
         * packageRoot не задан, возвращается dir
         */
        String getDirPackage() {
            if (UtString.empty(packageRoot)) {
                return dir
            }
            return UtFile.join(dir, getPackageRootDir())
        }

        /**
         * true - генерятся тестовые исходники
         */
        boolean test = false

        /**
         * Closure для генерации. В качестве параметра получает ссылку на GenTask.
         */
        Closure doTask

    }

    /**
     * Зависимости
     */
    LibDepends depends

    /**
     * Включать ли jandcode-jc в зависимости dev по умолчанию.
     * Нужно для полноценного редактирования jc-файлов.
     */
    boolean autoJcDepends = true

    /**
     * Каталоги с исходниками.
     */
    List<String> dirsSrc = ["src"]

    /**
     * каталоги с тестами
     */
    List<String> dirsSrcTest = ["test"]

    /**
     * В какой каталог будет компилироваться проект
     */
    String dirCompiled

    /**
     * В какой каталог будут компилироваться тесты
     */
    String dirCompiledTest

    String _packageRoot = ""

    /**
     * Имя корневого пакета
     */
    String getPackageRoot() {
        if (UtString.empty(_packageRoot)) {
            if (getModuleDefs().size() > 0) {
                return getModuleDefs().get(0).getName()
            }
        }
        return _packageRoot
    }

    void setPackageRoot(String packageRoot) {
        this._packageRoot = packageRoot
    }

    /**
     * Генерировать ли файл version.properties.
     * Для генерации необходимо задать packageRoot
     */
    boolean genVersionProperties = true

    /**
     * Файл манифеста MANIFEST.MF. Доступен после компиляции.
     */
    String fileManifest

    /**
     * Эти файлы считаются "исходниками". Все остальные - ресурсами.
     */
    List<String> includeSrc = ["**/*.java", "**/*.groovy"]

    /**
     * каталоги, которые исключаются из модуля и не видны в idea.
     */
    List<String> dirsExclude = ["temp", "lib", "_product", "_jc"]

    /**
     * Если true и в каталоге модуля есть подкаталог с jc-данными, то он включается в jar
     */
    boolean jcDataIncludeInJar = true

    /**
     * В какую группу входит модуль по умолчанию (для idea).
     * Если в RootProject не включен в какую-то группу, то он включается в эту,
     * если она определена.
     */
    String getModuleGroup() {
        return include(IdeVars).moduleGroup
    }

    void setModuleGroup(String v) {
        include(IdeVars).moduleGroup = v
    }

    /**
     * Маски файлов, которые исключаются из сборки в режиме prod
     * и не попадают в jar/src.zip
     */
    List<String> excludeSrcProd = ["**/_tst", "**/_tst/**/*"]

    /**
     * maven: groupId.
     * Если явно не указан = project.name
     */
    String groupId

    String getGroupId() {
        if (!UtString.empty(groupId)) {
            return groupId
        }
        Project root = getRootProject()
        if (root == null) {
            return project.name
        }
        RootProject rp = root.getIncluded(RootProject)
        if (rp != null) {
            String s = rp.getGroupId()
            if (UtString.empty(s)) {
                return project.name
            } else {
                return s
            }
        }
        return project.name
    }

    /**
     * packageRoot в виде пути (точки заменены на '/')
     */
    String getPackageRootDir() {
        return packageRoot.replace('.', '/')
    }

    /**
     * Полное имя получаемого после компиляции jar-файла.
     */
    String getFileJar() {
        return wd("temp/lib/${project.name}.jar")
    }

    /**
     * Полное имя получаемого после компиляции jar-файла.
     */
    String getFileSrczip() {
        return wd("temp/lib/${project.name}-src.zip")
    }

    /**
     * Каталоги с сгенеренными исходниками
     */
    List<String> getDirsSrcGen() {
        Set<String> res = new LinkedHashSet<>()
        for (t in _genTasks) {
            if (!t.test) {
                res.add(t.dir)
            }
        }
        return new ArrayList(res)
    }

    /**
     * Каталоги с сгенеренными исходниками тестов
     */
    List<String> getDirsSrcGenTest() {
        Set<String> res = new LinkedHashSet<>()
        for (t in _genTasks) {
            if (t.test) {
                res.add(t.dir)
            }
        }
        return new ArrayList(res)
    }

    //////

    boolean _compiledClasses = false
    boolean _compiledJar = false
    boolean _compiledSrczip = false
    boolean _compiledCm = false  // выполнялась ли команда compile

    // зарегистрированные задачи генерации исходников
    List<GenTask> _genTasks = new ArrayList<>()

    protected void onInclude() throws Exception {
        if (getIncluded(RootProject) != null) {
            error("Нельзя в одном проекте подключать RootProject и JavaProject")
        }

        this.depends = new LibDependsImpl(ctx, this)

        dirCompiled = wd("temp/compiled")
        dirCompiledTest = wd("temp/compiled.test")
        fileManifest = wd("temp/compiled/META-INF/MANIFEST.MF")

        //
        afterLoad(this.&onAfterLoad)
        afterLoadAll(this.&onAfterLoadAll)

        // utilites
        include(CreateProject)

        // showinfo
        include(Showinfo)
        onEvent(Showinfo.Event_GetInfo, this.&showinfoHandler)

        // showlibs
        include(Showlibs)

        // lib provider
        ctx.addLibProvider(new LibProviderJavaProject(this))

        // genidea
        include(GenIdea)
        onEvent(GenIdea.Event_GenIml, this.&genImlHandler)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)

        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)

        // tests
        include(JUnitProject)

        // clean
        include(CleanProject)
        onEvent(CleanProject.Event_BeforeClean, this.&cleanBeforeHandler)
        onEvent(CleanProject.Event_Clean, this.&cleanHandler)

        // build
        include(BuildProject)
        onEvent(BuildProject.Event_Build, this.&buildHandler)

        ////// cm
        cm.add("compile", this.&cmCompile, "Компиляция модуля")
        cm.add("compile-test", this.&compileTest, "Компиляция тестов модуля")
        cm.add("gen-idea", this.&cmGenIdea, "Генерация файлов idea",
                cm.opt("p", false, "Генерировать файл проекта. Если не указано, генерируется только модуль"),
        )
    }

    void onAfterLoad() {

        // обрабатываем moduleDef

        for (ModuleDefInfo md : getModuleDefInfos()) {

            // регистрируем
            this._moduleDefs.add(md.getModuleDef())

            // все зависимости из moduleDef - в зависимости JavaProject
            for (LibDependsGroup gr : md.getDepends().getGroups()) {
                this.depends.getGroup(gr.getName()).add(gr.getNames())
            }
            // все модули prod - в зависимости модуля
            md.getModuleDef().getDepends().addAll(md.getDepends().getProd().getModules())
        }

        ClasspathUsed cp = getIncluded(ClasspathUsed)
        if (cp != null) {
            // в процессе загрузки были вызовы classpath, включаем все в dev
            depends.dev.add(cp.usedClasspath)
        }

        // автодобавление jc в зависимости
        if (autoJcDepends) {
            String libJcName = "jandcode-jc"
            Lib libJc = ctx.findLib(libJcName)
            if (libJc != null) {
                // имеется jc
                ListLib jcDepends = libJc.depends.all.getLibsAll()
                if (jcDepends.find(project.name) == null) {
                    // у jc я в зависимостях не числюсь, добавляю jc себе в зависимости
                    depends.dev.add(libJcName)
                }
            }
        }
    }

    void onAfterLoadAll() {
        if (_genTasks.size() > 0) {
            // есть задачи генерации
            cm.add("gen-src", this.&cmGenSrc, "Генерация исходников")
        }
    }

    //////

    /**
     * Чистка проекта
     */
    void cleanBeforeHandler() {
        if (_compiledClasses) {
            log.info "Clean temp skipped (is compiled): ${project.name}"
            include(CleanProject).cleanTemp = false
        }
    }

    /**
     * Чистка проекта
     */
    void cleanHandler() {
        _compiledClasses = false
        _compiledJar = false
        _compiledSrczip = false
        _compiledCm = false
    }

    //////

    void cmCompile() {
        if (_compiledCm) {
            return
        }
        //
        compileClasses()
        compileJar()
        compileSrczip()
        //
        _compiledCm = true
    }

    //////

    /**
     * Компиляция классов.
     * Формируется каталог compiled
     */
    void compileClasses() {
        _compiledClasses = false
        _compiledJar = false
        _compiledSrczip = false
        _compiledCm = false

        // проверяем, что все зависимые библиотеки скомпилированы
        this.depends.all.libsAll.classpath

        // чистим все скомпилированное
        cm.exec("clean")
        //
        log.info("Compile: ${project.name}")
        // генерим исходники
        cmGenSrc()
        // уведомляем о начале компиляции
        project.fireEvent(new Event_BeforeCompile())
        // компиляция
        def cm = include(JavaCompiler)
        cm.compile(destdir: dirCompiled, srcs: dirsSrc + dirsSrcGen,
                libs: depends.all.libs, exclude_res: includeSrc)
        // манифест и version.properties
        defaultAfterCompile()
        // уведомляем. Тут можно модифицировать compiled
        project.fireEvent(new Event_AfterCompile())
        //
        _compiledClasses = true
    }

    /**
     * Проверка - изменились ли исходники по сравнению с targetFile.
     * @return true - исходники изменились
     */
    protected boolean checkIsSourceModified(String targetFile) {
        // дикое имя, т.к. ant не изменяет свойства после присвоения
        String propName = "tmp.compiled." + System.currentTimeMillis()
        //
        ant.uptodate(property: propName, targetfile: targetFile) {
            srcfiles(dir: UtFile.path(project.projectFile),
                    includes: UtFile.filename(project.projectFile))
            for (s in dirsSrc) {
                srcfiles(dir: s, includes: "**/*")
            }
        }
        String v = ant.project.getProperty(propName)
        if ("true".equals(v)) {
            return false
        } else {
            return true
        }
    }

    /**
     * Возвращает true, если проект скомпилирован
     */
    boolean isCompiledClasses() {
        if (_compiledClasses) {
            return true
        }
        boolean res = checkIsSourceModified(fileManifest)
        if (!res) {
            _compiledClasses = true
        } else {
            _compiledJar = false
            _compiledSrczip = false
        }
        return _compiledClasses
    }

    /**
     * Компиляция, если исходники изменились
     */
    void recompileClasses() {
        if (isCompiledClasses()) {
            return
        }
        compileClasses()
    }

    //////

    /**
     * Сборка jar
     */
    void compileJar() {
        if (!_compiledClasses) {
            compileClasses()
        }
        //
        String jcDataDir = wd(JcConsts.JC_DATA_DIR)
        boolean needIncludeJcData = false
        if (jcDataIncludeInJar) {
            if (UtFile.exists(jcDataDir) && UtFile.isDir(jcDataDir)) {
                needIncludeJcData = true

                // нужно включать jc-данные. Чистим перед включением
                String jcDataProjectFile = UtFile.join(jcDataDir, JcConsts.PROJECT_FILE)
                if (UtFile.exists(jcDataProjectFile)) {
                    def jcdataProject = load(jcDataProjectFile)
                    if (jcdataProject.cm.find("clean")) {
                        jcdataProject.cm.exec("clean")
                    }
                }
            }
        }
        //
        ant.jar(destfile: fileJar, manifest: fileManifest) {
            fileset(dir: dirCompiled) {
                include(name: '**/*')
                if (ctx.env.prod) {
                    for (String ex in excludeSrcProd) {
                        exclude(name: ex)
                    }
                }
            }
            if (needIncludeJcData) {
                log.info("include ${jcDataDir} in jar META-INF")
                zipfileset(dir: jcDataDir, prefix: "META-INF/${JcConsts.JC_DATA_DIR}", defaultexcludes: false) {
                    include(name: "**/*")
                    // не включаем project.jc из корня, он управляющий
                    exclude(name: "project.jc")
                }
            }
        }
        _compiledJar = true
    }

    /**
     * Возвращает true, если jar для проекта скомпилирована
     */
    boolean isCompiledJar() {
        if (!isCompiledClasses()) {
            return false
        }
        if (_compiledJar) {
            return true
        }
        // дикое имя, т.к. ant не изменяет свойства после присвоения
        String propName = "tmp.compiled." + System.currentTimeMillis()
        //
        ant.uptodate(property: propName, targetfile: fileJar, srcfile: fileManifest)
        //
        String v = ant.project.getProperty(propName)
        if ("true".equals(v)) {
            _compiledJar = true
        }
        return _compiledJar
    }

    /**
     * Компиляция, если исходники изменились
     */
    void recompileJar() {
        if (isCompiledJar()) {
            return
        }
        compileJar()
    }

    //////

    /**
     * Сборка srczip
     */
    void compileSrczip() {
        if (!_compiledClasses) {
            compileClasses()
        }
        // включаем только java+groovy, остальные и так имеются в jar
        ant.zip(destfile: fileSrczip, whenempty: "create") {
            for (s in dirsSrc + dirsSrcGen) {
                fileset(dir: s) {
                    for (String sr in includeSrc) {
                        include(name: sr)
                    }
                    if (ctx.env.prod) {
                        for (String ex in excludeSrcProd) {
                            exclude(name: ex)
                        }
                    }
                }
            }
        }
        _compiledSrczip = true
    }

    /**
     * Возвращает true, если srczip для проекта скомпилирована
     */
    boolean isCompiledSrczip() {
        if (!isCompiledClasses()) {
            return false
        }
        if (_compiledSrczip) {
            return true
        }
        // дикое имя, т.к. ant не изменяет свойства после присвоения
        String propName = "tmp.compiled." + System.currentTimeMillis()
        //
        ant.uptodate(property: propName, targetfile: fileSrczip, srcfile: fileManifest)
        //
        String v = ant.project.getProperty(propName)
        if ("true".equals(v)) {
            _compiledSrczip = true
        }
        return _compiledSrczip
    }

    /**
     * Компиляция, если исходники изменились
     */
    void recompileSrczip() {
        if (isCompiledSrczip()) {
            return
        }
        compileSrczip()
    }

    //////

    /**
     * По умолчанию после компиляции:
     * генерация manifest
     */
    void defaultAfterCompile() {
        // если нет манифеста, создаем
        if (!UtFile.exists(fileManifest)) {
            ut.cleanfile(fileManifest)
        }
        // правим манифест по умолчанию
        ant.manifest(file: fileManifest, mode: "update") {
            attribute(name: "Specification-Version", value: version)
            attribute(name: "Implementation-Version", value: version)

            // описание модулей
            if (getModuleDefs().size() > 0) {
                String s = UtString.join(UtCnv.toNameList(getModuleDefs()), ",")
                attribute(name: ModuleDefConsts.MANIFEST_MODULES, value: s)
            }

            attribute(name: JcConsts.MANIFEST_LIB_NAME, value: project.name)
            attribute(name: JcConsts.MANIFEST_LIB_VERSION, value: project.version)
            attribute(name: JcConsts.MANIFEST_LIB_GROUP_ID, value: getGroupId())

            // описание библиотеки
            for (LibDependsGroup g : depends.getGroups()) {
                if (!g.isEmpty()) {
                    attribute(name: JcConsts.MANIFEST_LIB_DEPENDS + "-" + g.getName(), value: UtString.join(g.libs.names, ","))
                }
            }

        }
        // version.properties
        if (!UtString.empty(packageRoot) && genVersionProperties) {
            // генерируем файл version.properties
            def vd = UtFile.join(dirCompiled, packageRootDir)
            ant.mkdir(dir: vd)
            ant.echo(message: "version=${version}", file: UtFile.join(vd, "version.properties"))
        }

        // moduleDefs
        if (getModuleDefs().size() > 0) {
            for (ModuleDef md : getModuleDefs()) {
                String dir = UtFile.join(dirCompiled, "META-INF/jc-module/${md.getName()}")
                String fn = UtFile.join(dir, "module-info.cfx")
                UtFile.mkdirs(dir)
                SimXml x = new SimXmlNode()
                x.setName("root")
                for (String dep : md.getDepends()) {
                    SimXml x1 = x.addChild("x-depends")
                    x1.attrs["module"] = dep
                }
                x.save().toFile(fn)
            }
        }
    }

    ////// unittest

    /**
     * компиляция тестов
     */
    void compileTest() {
        recompileClasses()
        if (dirsSrcTest.size() == 0) {
            return  // тестов нет
        }
        log.info "Compile tests: ${project.name}"
        //
        // уведомляем
        project.fireEvent(new Event_BeforeCompileTest())
        //
        def cm = include(JavaCompiler)
        cm.compile(destdir: dirCompiledTest, srcs: dirsSrcTest + dirsSrcGenTest,
                libs: depends.all.libs.names + [project.name], exclude_res: includeSrc)
        //
        // уведомляем
        project.fireEvent(new Event_AfterCompileTest())
    }

    ////// idea

    void cmGenIdea(CmArgs args) {
        // если имеется команда prepare, выполняем ее перед генерацией idea
        if (cm.find("prepare") != null) {
            cm.exec("prepare")
        }
        GenIdea g = include(GenIdea)
        g.genIml()
        if (args.p) {
            g.genIpr()
            g.genIws()
        }
    }

    void genImlHandler(GenIdea.Event_GenIml e) {
        ImlXml x = e.x
        //
        x.removeFolders()
        x.addFolders(dirsSrc, "src")
        x.addFolders(dirsSrcGen, "src.gen")
        x.addFolders(dirsSrcTest, "test")
        x.addFolders(dirsSrcGenTest, "test.gen")
        x.addFolders(dirsExclude, "exclude")
        //
        x.removeDepends()
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ListLib rlibs = deps.all.libsAll

        x.addDepends(rlibs)
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x
        //
        x.removeModules()
        //
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        deps.prod.add(project)
        ListLib rlibs = deps.all.libsAll

        x.addModules(rlibs)
    }

    ////// showinfo

    void showinfoHandler(Showinfo.Event_GetInfo e) {
        Map m = e.m
        //
        m['groupId'] = getGroupId()
        //
        for (LibDependsGroup g : depends.groups) {
            if (!g.isEmpty()) {
                m["Depends ${g.name}"] = g.libs.names
            }
        }

        m['Dirs src'] = getDirsSrc()
        m['Dirs srcTest'] = getDirsSrcTest()
        def a = getDirsSrcGen()
        if (a.size() > 0) {
            m['Dirs srcGen'] = a
        }
        a = getDirsSrcGenTest()
        if (a.size() > 0) {
            m['Dirs srcGenTest'] = a
        }
    }

    ////// генерация исходников

    /**
     * Добавить каталог и процесс генерации исходников
     * @param destdir куда будем генерировать
     * @param doTask closure с кодом генерации. В качестве параметра принимает
     * ссылку на объект JavaProject.GenTask
     */
    void genSrc(String destdir, Closure doTask) {
        GenTask t = new GenTask()
        t.dir = wd(destdir)
        t.test = false
        t.doTask = doTask
        _genTasks.add(t)
    }

    /**
     * Добавить каталог и процесс генерации тестовых исходников
     * @param destdir куда будем генерировать
     * @param doTask closure с кодом генерации. В качестве параметра принимает
     * ссылку на объект JavaProject.GenTask
     */
    void genSrcTest(String destdir, Closure doTask) {
        GenTask t = new GenTask()
        t.dir = wd(destdir)
        t.test = true
        t.doTask = doTask
        _genTasks.add(t)
    }

    /**
     * Команда генерации исходников
     */
    void cmGenSrc() {

        // собираем уникальные пути
        Set<String> dirs = new HashSet<>()
        for (GenTask t in _genTasks) {
            dirs.add(UtFile.abs(t.dir))
        }

        //clean
        for (String d : dirs) {
            ut.cleandir(d)
        }

        // генерация
        for (GenTask t in _genTasks) {
            log.info("gen-src to path [${t.dir}]")
            t.doTask(t)
        }

    }

    ////// module defs

    private NamedList<ModuleDef> _moduleDefs = new DefaultNamedList<>()
    private NamedList<ModuleDefInfo> _moduleDefInfos = new DefaultNamedList<>()

    private class ModuleDefInfoImpl extends Named implements ModuleDefInfo {

        LibDepends depends

        ModuleDef moduleDef

        ModuleDefInfoImpl(String name, String path, String packageRoot, String moduleFile) {
            this.setName(name)
            this.depends = new LibDependsImpl(ctx, JavaProject.this)
            this.moduleDef = new SrcModuleDef(name, path, packageRoot, moduleFile)
        }

    }

    private class SrcModuleDef extends ModuleDefImpl {

        private boolean propsInited

        SrcModuleDef(String name, String path, String packageRoot, String moduleFile) {
            super(name, path, packageRoot, moduleFile)
        }

        Map<String, Object> getProps() {
            Map<String, Object> props = super.getProps()
            if (!propsInited) {
                propsInited = true

                // пути
                props["path.project"] = wd("")

                int n

                n = 0
                for (s in dirsSrc) {
                    n++
                    props["path.src.${n}"] = wd(s)
                }
                n = 0
                for (s in dirsSrcGen) {
                    n++
                    props["path.srcgen.${n}"] = wd(s)
                }
                n = 0
                for (s in dirsSrcTest) {
                    n++
                    props["path.test.${n}"] = wd(s)
                }
                n = 0
                for (s in dirsSrcGenTest) {
                    n++
                    props["path.testgen.${n}"] = wd(s)
                }

            }
            return props
        }
    }

    /**
     * Объявление модуля.
     *
     * Метод объявлет, что в библиотеке содержится модуль с указанным именем.
     *
     * @param packageName имя пакета модула.
     */
    ModuleDefInfo moduleDef(String packageName) {
        ModuleDefInfo md = this._moduleDefs.find(packageName)
        if (md != null) {
            return md  // уже определен
        }
        if (dirsSrc.size() == 0) {
            error("В проекте нет dirSrc")
        }
        // берем первый путь в исходниках и считаем его корнем
        String root1 = wd(dirsSrc[0])
        String m_path = UtFile.abs(root1 + "/" + packageName.replace('.', '/'))
        String m_file = m_path + "/" + ModuleDefConsts.FILE_MODULE_CONF
        if (!UtFile.exists(m_file)) {
            error("В пакете ${packageName} нет файла ${ModuleDefConsts.FILE_MODULE_CONF}")
        }
        md = new ModuleDefInfoImpl(packageName, m_path, null, m_file)
        // автоматически добавляем зависимость, она всегда нужна
        if (md.name != "jandcode.core") {
            md.depends.prod.add("jandcode.core")
        }
        this._moduleDefInfos.add(md)

        return md
    }

    /**
     * Список ModuleDef зарегистрированных в проекте
     */
    List<ModuleDef> getModuleDefs() {
        return _moduleDefs
    }

    /**
     * Список ModuleDefInfo зарегистрированных в проекте
     * @return
     */
    NamedList<ModuleDefInfo> getModuleDefInfos() {
        return _moduleDefInfos
    }

    /**
     * Подготовка проекта к использованию
     */
    void prepareHandler() {
        // создаем каталоги src и test, т.к. без них комиляция не запустится, а они иногда не нужны
        for (d in dirsSrc + dirsSrcTest) {
            String dir = wd(d)
            if (!UtFile.exists(dir)) {
                ant.mkdir(dir: dir)
            }
        }

        // также создаем каталоги для генерации, что бы ide их сразу видела
        for (GenTask t in _genTasks) {
            if (!UtFile.exists(t.dir)) {
                ant.mkdir(dir: t.dir)
            }
        }

    }

    void buildHandler() {
        cm.exec("compile")
    }

}
