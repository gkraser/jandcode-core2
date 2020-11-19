package jandcode.core.dbm.dict.data


import jandcode.core.dbm.dao.*
import jandcode.core.dbm.dict.*
import jandcode.core.dbm.mdb.*
import jandcode.core.store.*

class DummyDict extends BaseModelDao implements DictHandler {

    void resolveIds(Mdb mdb, DictData dictData, Collection ids) throws Exception {
        Store data = dictData.getData()
        for (id in ids) {
            data.add(id: id, text: "${dictData.name}-text-${id}")
        }
    }

}
