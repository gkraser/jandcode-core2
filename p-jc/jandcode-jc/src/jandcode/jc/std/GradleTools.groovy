package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.gradle.*

/**
 * gradle-обвязка для формирования библиотек.
 */
class GradleTools extends ProjectScript implements ILibDirBuilder {

    /**
     * Базовый временный каталог
     */
    String tempDir = "temp/gradle-tools"

    /**
     * gradle файл с описанием библиотек
     */
    String libBuildFile = "lib.gradle"

    /**
     * В какой каталог делать publish.
     * Если не установлен, команда publish не доступна
     */
    String publishDir;

    String settingsGradleScript = "res:jandcode/jc/std/gradle/gradle-tools-settings.gradle.txt"
    String baseGradleScript = "res:jandcode/jc/std/gradle/gradle-tools-base.gradle.txt"

    /**
     * Дополнительные файлы, изменения в которых должны приводить к пересборки
     * проекта.
     */
    List<String> markFiles = []

    private List<Closure> _filters = []


    void onInclude() {
        //
        cm.add("gradle-prepare", this.&cmPrepare, "Подготовить временный каталог для gradle")
        cm.add("gradle-build", this.&cmBuild, "Собрать описанные библиотеки")

        cm.add("gradle-check", this.&cmCheck, "Проверить новые версии описанных библиотек")
        cm.add("gradle-showdeps", this.&cmShowDeps, "Показать зависимости, определенные в gradle")

        //
        afterLoad {
            if (!UtString.empty(publishDir)) {
                cm.add("gradle-publish", this.&cmPublish, "Публикация описанных библиотек")
            }
        }
    }

    class LibDefHolder {
        List<GradleLibDef> items = []

        List<String> warns = []

        /**
         * Добавить из xml, полученного из gradle
         */
        void addAllFromXml(SimXml x) {
            for (SimXml x1 : x.childs) {
                addOneFromXml(x1)
            }
        }

        void addOneFromXml(SimXml x) {
            GradleLibDef lib = new GradleLibDef()
            lib.name = x['module']
            lib.displayName = x['displayName']
            lib.module = x['module']
            lib.group = x['group']
            lib.version = x['version']
            lib.gradleDepends.addAll(UtCnv.toList(x['depends']))
            List tmp = UtCnv.toList(x['jars'])
            if (tmp.size() != 1) {
                warns.add("Для библиотеки ${lib.displayName} обнаружено ${tmp.size()} jar-файлов")
            }
            if (tmp.size() > 0) {
                lib.gradleJar = tmp[0]
            }
            tmp = UtCnv.toList(x['srcs'])
            if (tmp.size() != 1) {
                warns.add("Для библиотеки ${lib.displayName} обнаружено ${tmp.size()} src-файлов")
            }
            if (tmp.size() > 0) {
                lib.gradleSrc = tmp[0]
            }
            items.add(lib)
        }

        void prepare() {
            List<GradleLibDef> tmp = []
            for (GradleLibDef lib : this.items) {
                if (!lib.include) {
                    continue  // такая не нужна
                }
                if (UtString.empty(lib.gradleJar)) {
                    continue // нет jar - не нужна
                }
                if (UtString.empty(lib.version)) {
                    lib.version = '0.1-SNAPSHOT'
                }
                tmp.add(lib)
            }
            this.items = tmp

            // проверка на дубли
            Map<String, GradleLibDef> tmpNames = new LinkedHashMap<>()
            Map<String, GradleLibDef> tmpDisplayNames = new LinkedHashMap<>()
            for (GradleLibDef lib : this.items) {
                GradleLibDef libExists = tmpNames.get(lib.name)
                if (libExists != null) {
                    warns.add("Для модуля ${lib.displayName} обнаружен дубль ${libExists} с именем библиотеки ${lib.name}")
                    continue
                }
                tmpNames.put(lib.name, lib)
                tmpDisplayNames.put(lib.displayName, lib)
            }
            this.items.clear()
            this.items.addAll(tmpNames.values())

            // формируем правильные зависимости
            for (GradleLibDef lib : this.items) {
                for (String depGradle : lib.gradleDepends) {
                    GradleLibDef libDep = tmpDisplayNames.get(depGradle)
                    if (libDep != null && libDep.name != lib.name) {
                        lib.depends.add(libDep.name)
                    }
                }
            }

            // сотртируем
            this.items.sort { a, b -> a.name <=> b.name }
        }

        void saveToLibXml(String fn) {
            SimXml x = new SimXmlNode()
            for (GradleLibDef lib : this.items) {
                SimXml x1 = x.addChild("lib")
                x1['name'] = lib.name
                x1['version'] = lib.version
                x1['artifactId'] = lib.module
                x1['groupId'] = lib.group
                if (lib.depends.size() > 0) {
                    x1['depends.prod'] = UtString.join(lib.depends, ',')
                }
                if (!UtString.empty(lib.jar)) {
                    x1['jar'] = lib.jar
                }
                if (!UtString.empty(lib.src)) {
                    x1['src'] = lib.src
                }
            }
            def saver = new SimXmlSaver(x)
            saver.setColWordWrap(1000)  // для удобства чтения
            saver.save().toFile(fn)
        }

        void warnsPrint() {
            for (w in warns) {
                log.warn(w)
            }
            warns.clear()
        }
    }

    List<String> buildLibDir() {
        if (UtString.empty(publishDir)) {
            return null // каталог публикации не указан
        }

        //
        List<String> res = [wd(publishDir)]

        if (!markFilePublish.exists()) {
            // строим
            cmBuild()
            // публикуем
            cmPublish()
        }

        return res
    }

    void prepareGradleTempDir() {
        String gradleWorkDir = wd(tempDir)
        ant.mkdir(dir: gradleWorkDir)

        // settings.gradle
        String txt = UtFile.loadString(settingsGradleScript)
        UtFile.saveString(txt, new File(UtFile.join(gradleWorkDir, "settings.gradle")))

        // base.gradle
        txt = UtFile.loadString(baseGradleScript)
        UtFile.saveString(txt, new File(UtFile.join(gradleWorkDir, "base.gradle")))

        // build.gradle
        String f2 = wd(libBuildFile).replace('\\', '/')
        String gradleScript = "apply from: new File('./base.gradle')\n" +
                "apply from: new File('${f2}')\n"
        UtFile.saveString(gradleScript, new File(UtFile.join(gradleWorkDir, "build.gradle")))
    }

    SimXml loadGradleDepsXml() {
        prepareGradleTempDir()
        //
        String depsXmlFile = UtFile.join(wd(tempDir), "deps.xml")
        ant.delete(file: depsXmlFile)
        //
        ut.runcmd(cmd: "gradle resolveDeps", dir: wd(tempDir))
        //
        SimXml x = new SimXmlNode()
        x.load().fromFile(depsXmlFile)
        //
        return x
    }

    /**
     * Сюда будем собирать
     */
    protected String getOutDir() {
        return wd("${getTempDir()}/lib")
    }

    private String _markKey

    /**
     * ключ для маркировки каталога с построенными библиотеками
     */
    protected String getMarkKey() {
        if (_markKey == null) {
            List<File> lst = []
            lst.add(new File(project.projectFile))
            lst.add(new File(wd(libBuildFile)))
            for (s in markFiles) {
                lst.add(new File(wd(s)))
            }
            String s = ""
            for (f in lst) {
                s += "" + f.lastModified() + "|" + f.size() + "|"
            }
            _markKey = UtString.md5Str(s)
        }
        return _markKey
    }

    /**
     * Файл для метки - publish отработан
     */
    protected File getMarkFilePublish() {
        return new File(UtFile.join(wd(publishDir), "_mark-publish-" + getMarkKey()))
    }

    /**
     * Файл для метки - build отработан
     */
    protected File getMarkFileBuild() {
        return new File(UtFile.join(wd(getTempDir()), "_mark-build-" + getMarkKey()))
    }

    void cmPrepare() {
        prepareGradleTempDir()
    }

    void cmBuild() {
        ut.cleandir(wd(tempDir))
        ut.cleandir(wd(getOutDir()))
        //
        SimXml x = loadGradleDepsXml()
        LibDefHolder libs = new LibDefHolder()
        libs.addAllFromXml(x)
        for (Closure filter : _filters) {
            filter(libs.items)
        }
        libs.prepare()
        libs.saveToLibXml(getOutDir() + "/index.lib.xml")

        // копируем
        for (GradleLibDef lib : libs.items) {
            if (!UtString.empty(lib.gradleJar)) {
                ant.copy(file: lib.gradleJar,
                        tofile: UtFile.join(getOutDir(), "${lib.name}.jar"),
                        preservelastmodified: true)
            }
            if (!UtString.empty(lib.gradleSrc)) {
                ant.copy(file: lib.gradleSrc,
                        tofile: UtFile.join(getOutDir(), "src/${lib.name}-src.zip"),
                        preservelastmodified: true)
            }
        }

        UtFile.saveString("", markFileBuild)

        libs.warnsPrint()
    }

    void cmPublish() {
        if (UtString.empty(publishDir)) {
            error("Каталог publishDir не указан")
        }

        ant.delete(file: markFilePublish.getAbsolutePath())

        if (!markFileBuild.exists()) {
            cmBuild()
        }
        //
        def dest = wd(publishDir)
        ut.cleandir(dest)
        ant.copy(todir: dest, preservelastmodified: true) {
            fileset(dir: wd(getOutDir()))
        }

        // метим как сделанную
        UtFile.saveString("", markFilePublish)

    }

    void cmCheck() {
        if (!markFileBuild.exists()) {
            cmBuild()
        }
        ut.runcmd(cmd: "gradle dependencyUpdates", dir: wd(tempDir))
    }

    void cmShowDeps() {
        if (!markFileBuild.exists()) {
            cmPrepare()
        }
        ut.runcmd(cmd: "gradle dependencies --configuration compile", dir: wd(tempDir))
    }

    /**
     * Добавить фильтр для обработки библиотек.
     * Closure при выполнении получит первым параметром список List<GradleLibDef>
     */
    void filter(Closure cls) {
        if (cls != null) {
            this._filters.add(cls)
        }
    }

}
