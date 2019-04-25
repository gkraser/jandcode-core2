package jandcode.store.impl;

import jandcode.commons.*;
import jandcode.store.*;
import jandcode.store.impl.json.*;
import jandcode.store.impl.outtable.*;

/**
 * Глобальная инициализация
 */
public class StaticInit {

    static {

        // json

        UtJson.getJsonEngine().getGsonBuilder().registerTypeHierarchyAdapter(
                StoreRecord.class, new StoreRecordAdapter());

        UtJson.getJsonEngine().getGsonBuilder().registerTypeHierarchyAdapter(
                Store.class, new StoreAdapter());

        // outtable

        UtOutTable.getOutTableEngine().registerOutTableFactory(
                new StoreOutTableFactory());

    }

}
