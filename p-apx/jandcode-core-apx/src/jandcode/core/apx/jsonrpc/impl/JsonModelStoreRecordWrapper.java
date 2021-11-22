package jandcode.core.apx.jsonrpc.impl;

import jandcode.core.store.*;

import java.util.*;

/**
 * wrapper для StoreRecord для конвертации в json
 */
public class JsonModelStoreRecordWrapper {

    // эти приватные поля используются конвертором в json!
    private StoreRecord records;
    private Map dictdata;

    public JsonModelStoreRecordWrapper(StoreRecord record) {
        this.records = record;
        this.dictdata = JsonModelUtils.dictdata(record.getStore());
    }

}
