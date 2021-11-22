package jandcode.core.apx.tst.dao;

import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.store.*;

/**
 * dao для тестирования store на клиенте
 */
public class StoreDao extends BaseModelDao {

    /**
     * Маленькое store
     */
    @DaoMethod
    public Store small() throws Exception {
        return genStore1(1, 5, 3);
    }

    /**
     * Произвольное store
     */
    @DaoMethod
    public Store custom(int countRecords, int countFields) throws Exception {
        if (countRecords > 100000) {
            countRecords = 100000; // ограничим
        }
        if (countFields > 1000) {
            countFields = 1000; // ограничим
        }
        return genStore1(1, countRecords, countFields);
    }

    //////

    private Store genStore1(long fromId, long countRecords, int countFields) {
        Store st = getMdb().createStore();
        st.addField("id", "long");
        st.addField("color", "long").setDict("color");
        st.addField("dict20", "long").setDict("dict20");

        for (int j = 1; j <= countFields; j++) {
            st.addField("f" + j, "string");
        }

        long id;
        for (int i = 0; i < countRecords; i++) {
            id = fromId + i;
            StoreRecord rec = st.add();
            rec.setValue("id", id);
            rec.setValue("color", (id % 3) + 1);
            rec.setValue("dict20", (id % 20) + 1);

            for (int j = 1; j <= countFields; j++) {
                rec.setValue("f" + j, "v-" + id + "-" + j);
            }
        }

        return st;
    }


}
