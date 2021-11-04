package jandcode.core.apx.jsonrpc.impl;

import jandcode.core.store.*;

import java.util.*;

/**
 * wrapper для StoreRecord для конвертации в json
 */
public class JsonModelStoreRecordWrapper {

    // эти приватные поля используются конвертором в json!
    private StoreRecord data;
    private Map dictdata;

    public JsonModelStoreRecordWrapper(StoreRecord record) {
        this.data = record;
        this.dictdata = JsonModelUtils.dictdata(record.getStore());
    }

}
