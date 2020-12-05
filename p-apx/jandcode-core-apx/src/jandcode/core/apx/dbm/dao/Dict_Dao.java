package jandcode.core.apx.dbm.dao;

import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Утилиты для словарей
 */
public class Dict_Dao extends BaseModelDao {

    @DaoMethod
    public Store loadDictData(String dictName, Collection<Object> ids) throws Exception {
        if (ids == null) {
            return getMdb().loadDictData(dictName).getData();
        } else {
            return getMdb().loadDictData(dictName, ids).getData();
        }
    }

}
