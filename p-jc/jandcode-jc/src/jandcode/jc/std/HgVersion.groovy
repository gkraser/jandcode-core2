package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.impl.version.*

/**
 * Работа с версией, получаемой из hg
 */
class HgVersion extends ProjectScript {

    Ver verCached

    class Ver extends BaseVersion {

        String cache
        String template

        Ver(String template) {
            this.template = template
        }

        String getText() {
            if (cache == null) {
                cache = UtString.substVar(template, "#{", "}") { v ->
                    if (v == "revno") {
                        return revno
                    } else if (v == "ver") {
                        return getVer()
                    } else {
                        return ""
                    }
                }
            }
            return cache
        }

    }

    String workDir

    /**
     * Рабочий каталог, относительно которого будет работать hg
     */
    String getWorkDir() {
        if (this.workDir == null) {
            return project.wd("")
        }
        return workDir
    }

    /**
     * Номер ревизии исходников
     */
    public String getRevno() {
        try {
            def f = UtFile.findFileUp(".hg", getWorkDir())
            if (f != null) {
                def a = ut.runcmd(cmd: 'hg identify -n', saveout: true, showout: false, dir: getWorkDir())
                if (a.size() > 0) {
                    def s = a[0]
                    s = s.replace('+', 'DEV')
                    return s
                }
            }
        } catch (ignore) {
        }
        return "DEV"
    }

    /**
     * Автогенерируемая версия (по тегам в hg) для рабочего каталога проекта.
     * Если текущий тег 'v-XXX', то возвращает 'XXX', иначе возвращает
     * версию 'XXX-rREVNO' из последнего тега 'v-XXX'
     */
    public String getVer() {
        def v = ""
        try {
            def f = UtFile.findFileUp(".hg", getWorkDir())
            if (f != null) {
                def a = ut.runcmd(cmd: 'hg identify -t', saveout: true, showout: false, dir: getWorkDir())
                v = ""
                if (a.size() > 0) {
                    if (a[0].startsWith("v-")) {
                        v = a[0].substring(2)
                    }
                }
            }
        } catch (ignore) {
        }
        if (v == "") {
            def vers = versions
            if (vers.size() > 0) {
                v = vers[0] + "-r" + revno
            } else {
                v = JcConsts.VERSION_DEFAULT + "-r" + revno
            }
        }
        return v
    }

    /**
     * Автогенерируемая версия (по тегам в hg).
     * Если текущий тег 'v-XXX', то возвращает 'XXX', иначе возвращает
     * версию 'XXX-rREVNO' из последнего тега 'v-XXX'
     */
    public IVersion getVersion() {
        if (verCached == null) {
            verCached = new Ver("#{ver}")
        }
        return verCached
    }

    /**
     * Версия по шаблону. Переменные в шаблоне #{VAR}:
     * revno - номер ревизии рабочего каталога
     * ver - номер версии вычмисленно по рабочему каталогу
     */
    public IVersion version(String template) {
        return new Ver(template)
    }

    /**
     * Возвращает список версий. Первая в списке - самая последняя
     */
    public List getVersions() {
        def v = []
        try {
            def f = UtFile.findFileUp(".hg", getWorkDir())
            if (f != null) {
                def a = ut.runcmd(cmd: 'hg tags', saveout: true, showout: false, dir: getWorkDir())
                if (a.size() > 0) {
                    for (vs in a) {
                        def b = vs.split(" ")
                        if (b.length > 1) {
                            if (b[0].startsWith("v-")) {
                                v.add(b[0].substring(2))
                            }
                        }
                    }
                }
            }
        } catch (ignore) {
        }
        return v
    }

    /**
     * Возвращает последнюю версию (по тегам hg)
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
