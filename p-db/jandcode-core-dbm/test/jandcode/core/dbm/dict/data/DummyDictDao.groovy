package jandcode.core.dbm.dict.data

import jandcode.core.dao.*
import jandcode.core.dbm.dao.*
import jandcode.core.dbm.dict.*
import jandcode.core.store.*

class DummyDictDao extends BaseModelDao implements IResolveDict {

    @DaoMethod
    void resolveDict(Dict dict, Store data, Collection ids) {
        for (id in ids) {
            data.add(id: id, text: "${dict.name}-text-${id}")
        }
    }

}
