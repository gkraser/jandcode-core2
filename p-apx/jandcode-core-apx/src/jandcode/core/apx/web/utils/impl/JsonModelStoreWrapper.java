package jandcode.core.apx.web.utils.impl;

import jandcode.core.store.*;

import java.util.*;

/**
 * wrapper для Store для конвертации в json
 */
public class JsonModelStoreWrapper {

    private Store data;
    private Map dictdata;

    public JsonModelStoreWrapper(Store store) {
        this.data = store;
        this.dictdata = JsonModelUtils.dictdata(store);
    }

}
