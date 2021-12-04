package jandcode.core.apx.tst.dao;

import jandcode.commons.*;
import jandcode.commons.rnd.*;
import jandcode.core.apx.store.*;
import jandcode.core.dao.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.store.*;

/**
 * dao для тестирования store на клиенте
 */
public class StoreDao extends BaseModelDao {

    /**
     * Настройки генерируемого store
     */
    public static class StoreConfig {
        public long fromId = 1;
        public int incId = 1;
        public int countRecords;
        public int countFields;

        public int getCountRecords() {
            return countRecords <= 0 ? 50 : Math.min(countRecords, 100000);
        }

        public int getCountFields() {
            return countFields <= 0 ? 5 : Math.min(countFields, 1000);
        }

        public long getFromId() {
            return fromId <= 0 ? 1 : fromId;
        }

        public int getIncId() {
            return incId <= 0 ? 1 : incId;
        }
    }

    /**
     * Фильтр для store
     */
    public static class StoreFilter {
        public String text1;

        public boolean isTrue(StoreRecord rec) {
            if (UtString.empty(text1)) {
                return true;
            }
            String s = rec.getString("text1");
            return s.contains(text1);
        }

    }

    /**
     * Маленькое store
     */
    public Store small() throws Exception {
        return genStore1(1, 1, 5, 3);
    }

    /**
     * Произвольное store
     */
    public Store custom(StoreConfig config) throws Exception {
        return genStore1(config.getFromId(), config.getIncId(),
                config.getCountRecords(), config.getCountFields());
    }

    /**
     * Произвольное store с фильтрацией
     */
    public Store customFiltered(StoreConfig config, StoreFilter filter) throws Exception {
        Store st = genStore1(config.getFromId(), config.getIncId(),
                config.getCountRecords(), config.getCountFields());
        st.getRecords().removeIf(rec -> !filter.isTrue(rec));
        return st;
    }

    /**
     * Произвольное store с фильтрацией
     */
    public Store customPaginate(StoreConfig config, StoreFilter filter, Paginate paginate) throws Exception {
        // полный набор данных
        Store stSrc = genStore1(config.getFromId(), config.getIncId(),
                config.getCountRecords(), config.getCountFields());

        // фильтруем
        stSrc.getRecords().removeIf(rec -> !filter.isTrue(rec));

        // извлекаем запрашиваемую часть

        Store st = stSrc.cloneStore();
        Paginate resPaginate = new Paginate(
                paginate.getOffset(),
                paginate.getLimit(),
                stSrc.size()
        );
        ApxStoreUtils.setPaginate(st, resPaginate);

        int starRec = resPaginate.getOffset();
        int endRec = Math.min(starRec + resPaginate.getLimit(), stSrc.size());
        if (resPaginate.getLimit() == 0) {
            endRec = stSrc.size();
        }

        for (int i = starRec; i < endRec; i++) {
            st.add(stSrc.get(i));
        }

        return st;
    }

    //////

    private Store genStore1(long fromId, int incId, int countRecords, int countFields) {

        Rnd rnd = new Rnd(fromId);

        Store st = getMdb().createStore();
        st.addField("id", "long");
        st.addField("color", "long").setDict("color");
        st.addField("dict20", "long").setDict("dict20");
        st.addField("text1", "string");

        for (int j = 1; j <= countFields; j++) {
            st.addField("f" + j, "string");
        }

        String rndEnChars = "qwertyasdfg";

        long id = fromId;
        for (int i = 0; i < countRecords; i++) {
            StoreRecord rec = st.add();
            rec.setValue("id", id);
            rec.setValue("color", (id % 3) + 1);
            rec.setValue("dict20", (id % 20) + 1);
            rec.setValue("text1", rnd.text(rndEnChars, 10, 15, 4));

            for (int j = 1; j <= countFields; j++) {
                rec.setValue("f" + j, "v-" + id + "-" + j);
            }

            id = id + incId;
        }

        return st;
    }


}
