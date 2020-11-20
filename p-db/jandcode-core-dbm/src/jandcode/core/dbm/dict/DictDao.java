package jandcode.core.dbm.dict;

import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;

import java.util.*;

/**
 * dao для загрузки словарей
 */
public class DictDao extends BaseModelDao {

    public static class DictIds {

        private Dict dict;
        private Collection<Object> ids;
        private DictData dictData;

        public DictIds(Dict dict, Collection<Object> ids) {
            this.dict = dict;
            this.ids = ids;
            this.dictData = dict.createDictData();
        }

        public Dict getDict() {
            return dict;
        }

        public Collection<Object> getIds() {
            return ids;
        }

        public DictData getDictData() {
            return dictData;
        }
    }

    /**
     * Заресолвить указанные id
     */
    @DaoMethod
    public DictData resolveIds(Dict dict, Collection ids) throws Exception {
        DictData dictData = dict.createDictData();
        if (ids != null && ids.size() > 0) {
            dict.getHandler().resolveIds(getMdb(), dict, dictData.getData(), ids);
        }
        return dictData;
    }

    /**
     * Заресолвить указанные id для всех переданных словарей
     */
    @DaoMethod
    public void resolveListIds(List<DictIds> lst) throws Exception {
        for (DictIds d : lst) {
            if (d.ids != null && d.ids.size() > 0) {
                d.dict.getHandler().resolveIds(getMdb(), d.dict, d.dictData.getData(), d.ids);
            }
        }
    }

}
