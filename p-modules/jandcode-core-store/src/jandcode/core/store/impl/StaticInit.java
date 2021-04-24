package jandcode.core.store.impl;

import jandcode.commons.*;
import jandcode.core.store.*;
import jandcode.core.store.impl.json.*;
import jandcode.core.store.impl.outtable.*;

/**
 * Глобальная инициализация
 */
public class StaticInit {

    static {

        // json

        UtJson.getJsonEngine().addGsonBuilderIniter(gsonBuilder -> {
            gsonBuilder.registerTypeHierarchyAdapter(
                    StoreRecord.class, new StoreRecordAdapter());
            gsonBuilder.registerTypeHierarchyAdapter(
                    Store.class, new StoreAdapter());
        });

        // outtable

        UtOutTable.getOutTableEngine().registerOutTableFactory(
                new StoreOutTableFactory());

    }

}
