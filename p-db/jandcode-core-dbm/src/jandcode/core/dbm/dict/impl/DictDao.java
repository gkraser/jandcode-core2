package jandcode.core.dbm.dict.impl;

import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import org.slf4j.*;

import java.util.*;

/**
 * dao для загрузки словарей.
 * Используется сервисом словарей для собственных внутренних нужд.
 */
public class DictDao extends BaseModelDao {

    protected static Logger log = LoggerFactory.getLogger(DictDao.class);

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
    public DictData resolveIds(Dict dict, Collection<Object> ids) throws Exception {
        DictData dictData = dict.createDictData();
        if (ids != null && ids.size() > 0) {
            dict.getHandler().resolveIds(getMdb(), dict, dictData.getData(), ids);
        }
        return dictData;
    }

    /**
     * Заресолвить указанные id для всех переданных словарей
     */
    public void resolveListIds(List<DictIds> lst) throws Exception {
        for (DictIds d : lst) {
            if (d.ids != null && d.ids.size() > 0) {

                if (log.isInfoEnabled()) {
                    Collection<Object> tmp = d.ids;
                    if (tmp.size() > 10) {
                        tmp = new ArrayList<>(d.ids).subList(1, 10);
                    }
                    String s = "" + tmp;
                    if (d.ids.size() > tmp.size()) {
                        s = s + "...";
                    }
                    log.info("dict: " + d.getDict().getName() + ", size " + d.ids.size() + ", ids " + s);
                }

                d.dict.getHandler().resolveIds(getMdb(), d.dict, d.dictData.getData(), d.ids);
            }
        }
    }

    /**
     * Загрузить загружаемый словарь.
     */
    public DictData loadDict(Dict dict) throws Exception {
        IDictHandlerLoadDict h = (IDictHandlerLoadDict) dict.getHandler();
        DictData res = dict.createDictData();
        h.loadDict(getMdb(), dict, res.getData());
        return res;
    }

}
