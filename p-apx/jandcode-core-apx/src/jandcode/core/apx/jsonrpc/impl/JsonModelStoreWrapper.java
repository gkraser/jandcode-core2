package jandcode.core.apx.jsonrpc.impl;

import jandcode.core.store.*;

import java.util.*;

/**
 * wrapper для Store для конвертации в json
 */
public class JsonModelStoreWrapper {

    private Store records;
    private Map dictdata;

    public JsonModelStoreWrapper(Store store) {
        this.records = store;
        this.dictdata = JsonModelUtils.dictdata(store);
    }

}
