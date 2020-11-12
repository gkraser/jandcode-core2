package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.impl.version.*

/**
 * Работа с версией, получаемой из git
 */
class GitVersion extends ProjectScript {

    Ver verCached

    class Ver extends BaseVersion {

        String cache

        Ver() {
        }

        String getText() {
            if (cache == null) {
                cache = getVer()
            }
            return cache
        }

    }

    String workDir

    /**
     * Рабочий каталог, относительно которого будет работать git
     */
    String getWorkDir() {
        if (this.workDir == null) {
            return project.wd("")
        }
        return workDir
    }

    /**
     * Автогенерируемая версия (по тегам в git) для рабочего каталога проекта.
     * Если текущий тег 'v-XXX', то возвращает 'XXX', иначе возвращает
     * версию 'XXX-COMMITHASH' из последнего тега 'v-XXX'
     */
    public String getVer() {
        def v = ""
        try {
            def f = UtFile.findFileUp(".git", getWorkDir())
            if (f != null) {
                def a = ut.runcmd(cmd: 'git describe --match "v-*"', saveout: true, showout: false, dir: getWorkDir())
                v = ""
                boolean hasHash = false
                if (a.size() > 0) {
                    if (a[0].startsWith("v-")) {
                        v = a[0].substring(2)
                        // убираем hash
                        int b = v.lastIndexOf('-g', v.length() - 1)
                        if (b != -1) {
                            hasHash = true
                            v = v.substring(0, b)
                        }
                    }
                }
                if (hasHash && !UtString.empty(v)) {
                    String branch = getBranch()
                    branch = branch.replace("/", "-")
                    if ("master" != branch) {
                        v = v + "-${branch}-${getRev()}"
                    }
                }
            }
        } catch (ignore) {
        }
        if (v == "") {
            v = JcConsts.VERSION_DEFAULT + "-" + getRev()
        }
        return v
    }

    /**
     * Номер ревизии исходников.
     * hash текущего коммита.
     */
    public String getRev() {
        try {
            def f = UtFile.findFileUp(".git", getWorkDir())
            if (f != null) {
                def a = ut.runcmd(cmd: 'git log --pretty=format:%h -n 1', saveout: true, showout: false, dir: getWorkDir())
                if (a.size() > 0) {
                    def s = a[0]
                    return s
                }
            }
        } catch (ignore) {
        }
        return "DEV"
    }

    /**
     * Имя ветки
     */
    String getBranch() {
        try {
            def f = UtFile.findFileUp(".git", getWorkDir())
            if (f != null) {
                def a = ut.runcmd(cmd: 'git branch --show-current', saveout: true, showout: false, dir: getWorkDir())
                if (a.size() > 0) {
                    def s = a[0]
                    return s
                }
            }
        } catch (ignore) {
        }
        return ""
    }

    /**
     * Автогенерируемая версия (по тегам в git).
     * Если текущий тег 'v-XXX', то возвращает 'XXX', иначе возвращает
     * версию 'XXX-COMMITHASH' из последнего тега 'v-XXX'
     */
    public IVersion getVersion() {
        if (verCached == null) {
            verCached = new Ver()
        }
        return verCached
    }

    /**
     * Возвращает список версий. Первая в списке - самая последняя
     */
    public List getVersions() {
        def v = []
        try {
            def f = UtFile.findFileUp(".git", getWorkDir())
            if (f != null) {
                def a = ut.runcmd(cmd: 'git tag --sort=-creatordate', saveout: true, showout: false, dir: getWorkDir())
                if (a.size() > 0) {
                    for (vs in a) {
                        if (vs.startsWith("v-")) {
                            v.add(vs.substring(2))
                        }
                    }
                }
            }
        } catch (ignore) {
        }
        return v
    }

    /**
     * Возвращает последнюю версию (по тегам git)
     */
    public String getLastVersion() {
        def v = versions
        if (v.size() == 0) {
            return ""
        } else {
            return v[0]
        }
    }

}
