package jandcode.core.apx.dao;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Доступ к словарям
 */
public class Dict_Dao extends BaseModelDao {

    /**
     * Получить данные для словаря
     *
     * @param dictName какой словарь
     * @param ids      набор id, которые нужно получить или null для всех данных
     */
    public Store loadData(String dictName, Collection<Object> ids) throws Exception {
        Model model = getModel();
        DictService dictScv = model.bean(DictService.class);
        Dict dict = dictScv.getDict(dictName);
        Store st;
        if (ids == null) {
            if (!dict.isLoadable()) {
                throw new XError("Словарь {} не загружаемый, полностью загрузить данные нельзя", dictName);
            }
            st = dictScv.loadDictData(dictName).getData();
        } else {
            st = dictScv.loadDictData(dictName, ids).getData();
        }

        // метим именем словаря
        st.setCustomProp("dict", dict.getName());

        return st;
    }

    /**
     * Получить данные для словарей
     *
     * @param dictdata данные словаря в виде {dictName:{id:ANY}}
     * @return пустое store, у которого заполнены dictdata и dictfields
     */
    public Store resolveDicts(Map dictdata) throws Exception {
        Store st = getMdb().createStore();
        if (dictdata == null) {
            return st;
        }

        // заполняем store данными, как будто имеются значения в словарных полях
        for (Object k : dictdata.keySet()) {
            String dictName = UtCnv.toString(k);
            Dict dict = getMdb().getDicts().find(dictName);
            if (dict == null) {
                continue;
            }
            StoreField f = st.addField(dict.getName(), dict.getDomain().getField("id").getStoreDataType().getName());
            f.setDict(dict.getName());
            Object values = dictdata.get(k);
            if (values instanceof Map mapValues) {
                for (Object valueId : mapValues.keySet()) {
                    StoreRecord rec = st.add();
                    rec.setValue(dict.getName(), valueId);
                }
            }
        }

        // распознаем все словари
        getMdb().resolveDicts(st);
        // а данные нам не нужны
        st.clear();
        //
        return st;
    }

}
