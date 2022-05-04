package jandcode.core.apx.dao;

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

}
