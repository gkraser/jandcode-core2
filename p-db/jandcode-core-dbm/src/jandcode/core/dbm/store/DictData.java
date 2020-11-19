package jandcode.core.dbm.store;

import jandcode.core.dbm.*;
import jandcode.core.store.*;

public class DictData extends BaseModelMember implements IStoreDictResolver {

    public Object getDictValue(String dictName, Object key, String dictFieldName) {
        return "(" + key + ":" + dictName + ":" + dictFieldName + ")";
    }

}
