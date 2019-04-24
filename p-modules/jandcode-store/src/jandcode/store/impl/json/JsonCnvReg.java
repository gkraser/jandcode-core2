package jandcode.store.impl.json;

import jandcode.commons.*;
import jandcode.store.*;

/**
 * Регистрация json-конверторов для store
 */
public class JsonCnvReg {

    static {

        UtJson.getJsonEngine().getGsonBuilder().registerTypeHierarchyAdapter(
                StoreRecord.class, new StoreRecordAdapter());

        UtJson.getJsonEngine().getGsonBuilder().registerTypeHierarchyAdapter(
                Store.class, new StoreAdapter());

    }

}
