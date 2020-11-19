package jandcode.core.dbm.dict.impl;

import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

public class DictDataImpl implements DictData {

    private Dict dict;
    private Store data;

    public DictDataImpl(Dict dict, Store data) {
        this.dict = dict;
        this.data = data;
    }

    public String getName() {
        return dict.getName();
    }

    public Dict getDict() {
        return dict;
    }

    public Store getData() {
        return data;
    }
}
