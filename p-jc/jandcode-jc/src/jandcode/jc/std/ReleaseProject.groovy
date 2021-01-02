package jandcode.jc.std

import jandcode.commons.conf.*
import jandcode.jc.*

/**
 * Проект для скриптов, собирающий релизы.
 * Предполагает, что все проекты лежат на git.
 */
class ReleaseProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(ConfigProject)
        include(GitRepos)

        //
        cm.add("step-update-repos", "Обновить все репозитории", this.&step_update_repos)
    }

    /**
     * Конфигурация
     */
    Conf getCfg() {
        return include(ConfigProject).getCfg()
    }

    /**
     * Конфигурация по умолчанию.
     * @param cls параметр: cfg
     */
    void defaultCfg(Closure cls) {
        include(ConfigProject).defaultCfg(cls)
    }

    /**
     * Репозитории
     */
    GitRepos getRepos() {
        return include(GitRepos)
    }

    /**
     * Конфигурация
     */
    ConfigProject getConfig() {
        return include(ConfigProject)
    }

    void step_update_repos() {
        repos.updateRepos()
    }

    /**
     * Создать построитель bat-файлов
     */
    BatBuilder createBatBuilder() {
        return create(BatBuilder)
    }

}
