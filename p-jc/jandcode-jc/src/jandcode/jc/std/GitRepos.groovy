package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.conf.*
import jandcode.commons.named.*
import jandcode.jc.*

/**
 * Репозитории git.
 * Используется в скриптах, собирающих релизы.
 */
class GitRepos extends ProjectScript {

    protected void onInclude() throws Exception {

        cm.add("repos-show", "Показать репозитории", this.&cmReposShow)

        cm.add("step-update-repos", "Обновить все репозитории", this.&updateRepos)
    }

    /**
     * Описание репозитория
     */
    class Repo implements INamed {

        /**
         * Имя репозитория.
         * Если не задано - берется из последней части url
         */
        String getName() {
            return _name
        }
        private String _name

        /**
         * Путь относительно каталога {@link GitRepos#reposDir}.
         * Если не задано = name
         */
        String path

        /**
         * url репозитория
         */
        String url

        /**
         * Ветка. Если не задано = master
         */
        String branch

        /**
         * Дополнительные опции для клонирования репозитория.
         * Например '--depth 1'.
         */
        String cloneOpts = ""

        /**
         * Дополнительные опции для checkout.
         */
        String checkoutOpts = ""

        /**
         * Дополнительные опции для pull.
         */
        String pullOpts = ""

        /**
         * Произвольные атрибуты для репозитория
         */
        Map<String, Object> attrs = [:]

        private boolean _updated

        /**
         * @param url url репозитория по умолчанию
         * @param name имя этого экземпляра. Если не задано, берется из
         * последней части url
         */
        Repo(String url, String name) {
            this.url = url
            this._name = name
            if (UtString.empty(this._name)) {
                this._name = UtFile.filename(this.url)
            }
        }

        String getPath() {
            if (!this.path) {
                return wd("${reposDir}/${getName()}")
            }
            if (UtFile.isAbsolute(this.path)) {
                return this.path
            }
            return wd("${reposDir}/${this.path}")
        }

        String getBranch() {
            if (!this.branch) {
                return "master"
            }
            return branch
        }

        /**
         * Обновить или клонировать репозиторий
         */
        void update(boolean force = false) {
            if (!force && this._updated) {
                return
            }
            if (UtFile.exists(getPath())) {
                ut.runcmd(cmd: "git checkout ${getCheckoutOpts()} ${getBranch()}", dir: getPath())
                ut.runcmd(cmd: "git pull --all -q ${getPullOpts()}", dir: getPath())
            } else {
                ut.runcmd(cmd: "git clone ${getCloneOpts()} ${getUrl()} ${getPath()}")
                ut.runcmd(cmd: "git checkout ${getCheckoutOpts()} ${getBranch()}", dir: getPath())
            }
            this._updated = true
        }

        /**
         * Загрузить конфигурацию репозитория из {@link CfgProject#getCfg()},
         * если он подключен в проекте.
         *
         * Конфигурация загружается из 'repo/NAME'.
         */
        void loadCfg() {
            CfgProject cfgProject = getIncluded(CfgProject)
            if (cfgProject) {
                Conf repoConf = cfgProject.cfg.findConf("repo/${getName()}")
                if (repoConf != null) {
                    UtReflect.getUtils().setProps(this, repoConf);
                }
            }
        }

        String toString() {
            return "Repo{" +
                    "name='" + getName() + '\'' +
                    ", path='" + getPath() + '\'' +
                    ", url='" + getUrl() + '\'' +
                    ", branch='" + getBranch() + '\'' +
                    '}';
        }
    }

    /**
     * Зарегистрированные репозитории
     */
    Map<String, Repo> repos = [:]

    /**
     * Каталог, где будут репозитории
     */
    String reposDir = "_repos"

    /**
     * Создать репозиторий
     * @param url откуда брать
     * @param name имя репозитория для дальнейшего доступа. Если не указана, то равно
     *             имени репозитория
     * @return экземпляр репозитория
     */
    Repo createRepo(String url, String name = "") {
        Repo r = new Repo(url, name)
        r.loadCfg()
        return r
    }

    /**
     * Добавить репозиторий
     * @see GitRepos#createRepo(java.lang.String, java.lang.String)
     * @return добавленный экземпляр {@link Repo}
     */
    Repo addRepo(String url, String name = "") {
        Repo r = createRepo(url, name)
        repos[r.name] = r
    }

    /**
     * Выполнить callback для каждого репозитория
     * @param cb
     */
    void eachRepo(Closure cb) {
        for (r in repos.values()) {
            ut.delim(r.name)
            cb(r)
        }
    }

    /**
     * Обновить все репозитории
     */
    void updateRepos() {
        eachRepo { Repo r ->
            r.update()
        }
    }

    /**
     * Загрузить версию из каталога.
     * Версия определяется по файлу VERSION. Если его нет, то по версии GitVersion.
     */
    String loadVersion(String path) {
        String fn = UtFile.join(path, "VERSION")
        if (UtFile.exists(fn)) {
            String ver = UtFile.loadString(fn).trim()
            return ver
        }
        GitVersion gv = create(GitVersion)
        gv.setWorkDir(path)
        return gv.getVersion().getText()
    }

    void cmReposShow(CmArgs args) {
        Map res = [:]
        for (r in repos.values()) {
            Map m = [:]
            res[r.name] = m
            m.url = r.url
            m.branch = r.branch
            m.path = r.path
            if (!UtString.empty(r.cloneOpts)) {
                m.cloneOpts = r.cloneOpts
            }
            if (!UtString.empty(r.pullOpts)) {
                m.pullOpts = r.pullOpts
            }
            if (!UtString.empty(r.checkoutOpts)) {
                m.checkoutOpts = r.checkoutOpts
            }
            if (r.attrs.size() > 0) {
                m.attrs = [:]
                m.attrs.putAll(r.attrs)
            }
        }

        //
        ut.printMap(res)
    }

}
