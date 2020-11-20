package jandcode.core.dbm.dict;

import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;

import java.util.*;

/**
 * dao для загрузки словарей
 */
public class DictDao extends BaseModelDao {

    /**
     * Заресолвить указанные id
     */
    @DaoMethod
    public DictData resolveIds(Dict dict, Collection ids) throws Exception {
        DictData dictData = dict.createDictData();
        dict.getHandler().resolveIds(getMdb(), dict, dictData.getData(), ids);
        return dictData;
    }

}
