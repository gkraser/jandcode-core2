package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Набор утилит для использования в скриптах,
 * собирающих релизы.
 *
 * Считается, что все собираемые проекты хранятся в git.
 */
class GitRelease extends ProjectScript {

    protected void onInclude() throws Exception {
        cm.add("step-update-repos", "Обновить все репозитории", this.&updateRepos)
    }

    /**
     * Описание репозитория
     */
    class Repo {

        /**
         * Имя репозитория.
         * Если не задано - берется из последней части url
         */
        String name

        /**
         * Путь относительно каталога {@link GitRelease#reposDir}.
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

        Repo(String url) {
            this.url = url
        }

        String getName() {
            if (!this.name) {
                return UtFile.filename(this.getUrl())
            }
            return name
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
        Repo r = new Repo(url)
        r.name = name
        return r
    }

    /**
     * Добавить репозиторий
     * @see GitRelease#createRepo(java.lang.String, java.lang.String)
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

}
