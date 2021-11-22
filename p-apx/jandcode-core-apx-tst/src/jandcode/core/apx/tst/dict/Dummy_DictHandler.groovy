package jandcode.core.apx.tst.dict

import jandcode.core.dbm.dict.*
import jandcode.core.dbm.mdb.*
import jandcode.core.store.*

/**
 * Загружает словарь размером countRecords со сгенеренными данными
 */
class Dummy_DictHandler extends BaseDictHandlerLoadable {

    void loadDict(Mdb mdb, Dict dict, Store data) throws Exception {
        int countRecords = dict.getConf().getInt("countRecords", 10)
        for (id in 1..countRecords) {
            def rec = data.add(id: id)
            for (f in rec.fields) {
                if (f.name == 'id') {
                    continue
                } else if (f.name == 'text') {
                    rec.setValue("text", dict.name + "-text-" + id)
                } else {
                    rec.setValue(f.name, f.name + "-" + id)
                }
            }
        }
    }

}
