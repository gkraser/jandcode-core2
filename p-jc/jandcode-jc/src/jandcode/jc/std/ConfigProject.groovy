package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.attrparser.*
import jandcode.commons.conf.*
import jandcode.jc.*

/**
 * Конфиг для проекта
 */
class ConfigProject extends ProjectScript {

    protected void onInclude() throws Exception {
        onEvent(Event_GrabDefaultCfg, this.&grabDefaultCfg)

        cm.add("cfg-show", "Показать конфигурацию", this.&cmCfgShow,
                cm.opt("g", false, "Создать файл с примером конфигурации с актуальными параметрами"),
        )
    }

    /**
     * Событие возникает при сборе конфигурации по умолчанию
     */
    static class Event_GrabDefaultCfg extends BaseJcEvent {

        /**
         * Собираемая конфигурация
         */
        Conf cfg

        Event_GrabDefaultCfg(Conf cfg) {
            this.cfg = cfg
        }

    }

    //////

    /**
     * Конфигурационный файл в корне проекта.
     * Загружаются если есть jc-cfg.cfx, затем _jc-cfg.cfx.
     * Формат: cfx. Забираются свойства из тега cfg.
     */
    String cfgFileName = "jc-cfg.cfx"

    /**
     * Префикс переменных для конфигурации.
     * Значение переменных строка вида 'a=b c=d'.
     * В качестве имени можно использовать путь, например: 'a/b/c=d'.
     * Переменные сортируются по имени.
     * Применяются самыми последними.
     *
     * Если значение переменной начинается со знака '@', то значение рассматривается
     * как имя файла с конфигурацией, которая будет подгружена. Забираютс свойства из
     * тега cfg.
     *
     * @see AttrParser
     */
    String envCfgPrefix = "JC_CFG_"

    private Conf _cfg

    Conf getCfg() {
        if (this._cfg != null) {
            return this._cfg
        }
        // еще не собрана, собираем
        Conf tmpCfg = UtConf.create()
        fireEvent(new Event_GrabDefaultCfg(tmpCfg))

        // загружаем jc-cfg.cfx, _jc-cfg.cfx
        for (fn in [wd("${cfgFileName}"), wd("_${cfgFileName}")]) {
            if (UtFile.exists(fn)) {
                log "load cfg from [${fn}]"
                Conf tmp = UtConf.create()
                UtConf.load(tmp).fromFile(fn)
                tmpCfg.join(tmp.findConf("cfg", true))
            }
        }

        // загружаем переменные JC_CFG_xxx
        Map<String, String> envCfg = new TreeMap<String, String>()
        Map<String, String> osenv = System.getenv();
        for (String key : osenv.keySet()) {
            String keyUp = key.toUpperCase()
            if (keyUp.startsWith(envCfgPrefix)) {
                log "use var [${key}] for load cfg"
                envCfg.put(keyUp, osenv.get(key))
            }
        }
        for (en in envCfg) {
            String value = en.getValue().trim()
            if (value.startsWith("@")) {
                String fn = wd(value.substring(1))
                if (UtFile.exists(fn)) {
                    log "load cfg from [${fn}]"
                    Conf cfgFromFile = UtConf.create()
                    UtConf.load(cfgFromFile).fromFile(fn)
                    tmpCfg.join(cfgFromFile.findConf("cfg", true))
                }
            } else {
                AttrParser p = new AttrParser()
                p.loadFrom(value)
                for (e2 in p.getResult()) {
                    tmpCfg.setValue(e2.getKey(), e2.getValue())
                }
            }
        }

        //
        this._cfg = tmpCfg
        return this._cfg
    }

    void grabDefaultCfg(Event_GrabDefaultCfg ev) {
        ev.cfg.setValue("java.home", System.properties['java.home'])
    }

    void cmCfgShow(CmArgs args) {

        boolean grab = args.containsKey("g")

        ut.delim("cfg")
        ut.printMap(getCfg())
        ut.delim()

        if (grab) {
            saveCfgExample(wd("temp/_${cfgFileName}"))
        }
    }

    /**
     * Конфигурация по умолчанию.
     * @param cls параметр: cfg
     */
    void defaultCfg(Closure cls) {
        if (cls != null) {
            onEvent(Event_GrabDefaultCfg) { ev ->
                cls(ev.cfg)
            }
        }
    }

    /**
     * Генерация _jc-cfg.cfx файла
     */
    void saveCfgExample(String cfgFile) {
        Conf cfg = getCfg()
        String s = ""
        for (key in cfg.keySet()) {
            s += "${key}=\"${UtString.xmlEscape(cfg.getString(key))}\"\n"
        }
        String content = """\
<?xml version="1.0" encoding="utf-8"?>
<root>
    <!--
    <cfg
${UtString.indent(s, 8)}
    />
    -->
    <cfg
    />

</root>
"""
        ut.cleanfile(cfgFile)
        UtFile.saveString(content, new File(cfgFile))
        log "Файл создан: ${cfgFile}"
    }

}
