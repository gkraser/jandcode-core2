package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.io.*
import jandcode.commons.named.*
import jandcode.jc.*

/**
 * Набор утилит для использования в скриптах,
 * собирающих релизы.
 *
 * Считается, что все собираемые проекты хранятся на mercurial.
 */
@Deprecated
class HgRelease extends ProjectScript {

    /**
     * Базовый адрес для репозиториев проектов
     */
    String baseRepo = "https://my-repo/my-basedir"

    /**
     * Базовый каталог, где будут располагатся собираемые проекты
     */
    String baseProjects = "_projects"

    /**
     * Все проекты, которые участвуют в сборке
     */
    NamedList<Prj> projects = new DefaultNamedList<>()

    /**
     * Маски удаляемый файлов при чистке проекта
     */
    List<String> cleanProjectMasks = [
            "**/_jc/**/*",
            "**/_jc",
            "**/_gen/**/*",
            "**/_gen",
            "**/temp/**/*",
            "**/temp",
    ]

    /**
     * Описание одного проекта
     */
    class Prj extends Named {

        /**
         * Произвольные атибуты проекта
         */
        Map<String, Object> attrs = [:]

        void setAttrs(Map<String, Object> attrs) {
            this.attrs.putAll(attrs)
        }

        /**
         * Каталог, в котором лежит локальная копия проекта.
         */
        String projectDir

        String getProjectDir() {
            if (this.projectDir == null) {
                return wd("${baseProjects}/${name}")
            }
            return this.projectDir
        }

        /**
         * Репозиторий hg, из которого берется проект
         */
        String srcRepo

        String getSrcRepo() {
            if (srcRepo == null) {
                return "${baseRepo}/${name}"
            }
            return srcRepo
        }

        /**
         * Утилиты для версий с использование hg для этого проекта
         */
        HgVersion getHgVersion() {
            if (_hgVersion == null) {
                _hgVersion = create(HgVersion)
                _hgVersion.workDir = getProjectDir()
            }
            return _hgVersion
        }
        private HgVersion _hgVersion

        /**
         * Обновить каталог с проектом до указанного тега.
         * @param rev на какой тег обновить. Если не указано, то считается, что
         * нужно обновить до последней тегированной версии.
         * @return тег , куда обновились фактически
         */
        String hg_updateTo(String rev) {
            if (UtString.empty(rev)) {
                rev = getHgVersion().lastVersion
                if (UtString.empty(rev)) {
                    rev = "tip"
                } else {
                    rev = "v-" + rev
                }
            }
            hg_update(getProjectDir(), rev)
            return rev
        }

    }

    //////

    /**
     * Создать экземпляр проекта
     * @param params свойства проекта, например name
     */
    Prj prj(Map params) {
        return new Prj(params)
    }

    /**
     * Разделитель
     * @param pdir для какого каталога
     * @param text текст
     */
    void delim(String pdir, String text) {
        ut.delim(text + ": " + UtFile.filename(pdir))
    }

    /**
     * Делает pull или clone для репозитория repo в каталог pdir.
     * Если каталог pdir не существует, делает clone, иначе pull.
     */
    void hg_pull(String pdir, String repo) {
        if (!UtFile.exists(pdir)) {
            delim(pdir, "hg clone")
            ut.runcmd(cmd: "hg clone ${repo} ${pdir}", showout: true, saveout: false)
        } else {
            delim(pdir, "hg pull")
            ut.runcmd(cmd: "hg pull -u", dir: pdir, showout: true, saveout: false)
        }
    }

    /**
     * hg_pull для всех проектов
     */
    void hg_pull_all() {
        ant.mkdir(dir: wd(baseProjects))
        for (pp in projects) {
            hg_pull(pp.projectDir, pp.srcRepo)
        }
    }

    /**
     * hg update на указанный тег или ревизию
     * @param pdir в каком каталоге
     * @param tag тег или ревизия
     */
    void hg_update(String pdir, String tag) {
        delim(pdir, "hg update ${tag}")
        ut.runcmd(cmd: "hg update ${tag}", dir: pdir)
    }

    /**
     * Проверка, что указанный каталог закомичен
     */
    boolean hg_check_commited(String dir) {
        List out = ut.runcmd(cmd: "hg status", dir: dir, saveout: true)
        return out.size() == 0
    }

    /**
     * Возвращает репозиторий по умолчанию для указанного каталога.
     * Это тот репозиторий, который указан в hgrc под именем default.
     * Возвращает пустую строку, если репозиторий не известен.
     */
    String hg_defaultRepo(String pdir) {
        List<String> a = ut.runcmd(cmd: "hg paths", dir: pdir, saveout: true, showout: false)
        String a2 = UtString.join(a, "\n")
        a2 = a2.replace('\\', '/')
        Properties p = new Properties()
        new PropertiesLoader(p).load().fromString(a2)
        String r = p.get("default")
        if (r == null) {
            return ""
        }
        return r
    }

    /**
     * Чистка проекта от явного мусора
     * @param pdir
     */
    void clean_project(String pdir) {
        ant.delete(includeemptydirs: true) {
            fileset(dir: pdir) {
                for (m in cleanProjectMasks) {
                    include(name: m)
                }
            }
        }
    }

    void clean_project_all() {
        ant.mkdir(dir: wd(baseProjects))
        for (pp in projects) {
            clean_project(pp.projectDir)
        }
    }

    /**
     * Загрузить файл с версией.
     * Такой файл обычно называется VERSION и содержит одну строку, которая
     * и возвращается. Пробелы обрезаются.
     * Если файла нет, возвращается пустая строка.
     * @param fn имя файла
     */
    String loadVersionFile(String fn) {
        String res = ""
        if (UtFile.exists(fn)) {
            res = UtFile.loadString(new File(fn)).trim()
        }
        return res
    }

    /**
     * Записать файл с версией
     * @param fn имя файла
     * @param ver версия
     */
    void saveVersionFile(String fn, String ver) {
        ant.echo(message: ver, file: fn)
    }

}
