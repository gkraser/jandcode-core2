package jandcode.core.db.impl;

import jandcode.core.db.*;
import jandcode.core.store.*;

/**
 * Статические утилиты для store + dbquery
 */
public class StoreUtils {

    /**
     * Создает store со структурой, соответствующей открытому запросу
     *
     * @param q открытый запрос
     */
    public static Store createStoreForQuery(DbQuery q) {
        StoreService svc = q.getDbSource().getApp().bean(StoreService.class);
        Store store = svc.createStore();
        for (DbQueryField qf : q.getFields()) {
            String dtn = qf.getDbDataType().getName();
            store.addField(qf.getName(), dtn);
        }
        return store;
    }

    /**
     * Загрузить данные из query в запись
     *
     * @param to   куда
     * @param from откуда
     */
    public static void loadRecordFromQuery(StoreRecord to, DbQuery from) {
        for (DbQueryField fromF : from.getFields()) {
            StoreField toF = to.findField(fromF.getName());
            if (toF != null) {
                if (!from.isNull(fromF.getIndex())) {
                    to.setValue(toF.getIndex(), from.getValue(fromF.getIndex()));
                } else {
                    to.setValue(toF.getIndex(), null);
                }
            }
        }
    }

    /**
     * Загрузить данные из query в store
     *
     * @param to   куда
     * @param from откуда
     */
    public static void loadStoreFromQuery(Store to, DbQuery from) throws Exception {

        // строим кеш
        int mx = to.getCountFields();
        int[] fromIdx = new int[mx];
        int[] toIdx = new int[mx];
        int pos = -1;
        for (DbQueryField fromF : from.getFields()) {
            StoreField toF = to.findField(fromF.getName());
            if (toF != null) {
                pos++;
                fromIdx[pos] = fromF.getIndex();
                toIdx[pos] = toF.getIndex();
            }
        }

        // переносим
        while (!from.eof()) {
            StoreRecord rec = to.add();
            for (int i = 0; i <= pos; i++) {
                if (!from.isNull(fromIdx[i])) {
                    rec.setValue(toIdx[i], from.getValue(fromIdx[i]));
                } else {
                    rec.setValue(toIdx[i], null);
                }
            }
            from.next();
        }
        //
    }


}
