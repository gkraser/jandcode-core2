package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.conf.*
import jandcode.jc.*

/**
 * Конфиг для проекта
 */
class CfgProject extends ProjectScript {

    protected void onInclude() throws Exception {
        onEvent(Event_GrabDefaultCfg, this.&grabDefaultCfg)

        cm.add("cfg-show", "Показать конфигурацию", this.&cmCfgShow)
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
     * Загружаются если есть cfg.cfx, затем _cfg.cfx.
     * Формат: cfx. Забираются свойства из тега cfg.
     */
    String cfgFileName = "cfg.cfx"

    private Conf _cfg

    Conf getCfg() {
        if (this._cfg != null) {
            return this._cfg
        }
        // еще не собрана, собираем
        Conf tmpCfg = UtConf.create()
        fireEvent(new Event_GrabDefaultCfg(tmpCfg))

        // загружаем cfg.cfx, _cfg.cfx
        for (fn in [wd("${cfgFileName}"), wd("_${cfgFileName}")]) {
            if (UtFile.exists(fn)) {
                Conf tmp = UtConf.create()
                UtConf.load(tmp).fromFile(fn)
                tmpCfg.join(tmp.findConf("cfg", true))
            }
        }

        //
        this._cfg = tmpCfg
        return this._cfg
    }

    void grabDefaultCfg(Event_GrabDefaultCfg ev) {
        ev.cfg.setValue("java.home", System.properties['java.home'])
    }

    void cmCfgShow() {
        ut.printMap(getCfg())
    }

}
