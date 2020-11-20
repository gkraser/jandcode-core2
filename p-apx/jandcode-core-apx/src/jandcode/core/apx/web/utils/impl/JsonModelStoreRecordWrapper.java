package jandcode.core.apx.web.utils.impl;

import jandcode.core.store.*;

import java.util.*;

/**
 * wrapper для StoreRecord для конвертации в json
 */
public class JsonModelStoreRecordWrapper {

    private StoreRecord data;
    private Map dictdata;

    public JsonModelStoreRecordWrapper(StoreRecord record) {
        this.data = record;
        this.dictdata = JsonModelUtils.dictdata(record.getStore());
    }

}
