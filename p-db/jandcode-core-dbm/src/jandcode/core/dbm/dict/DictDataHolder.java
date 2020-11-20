package jandcode.core.dbm.dict;

import jandcode.commons.named.*;
import jandcode.core.store.*;

/**
 * Хранилище DictData
 */
public interface DictDataHolder extends IStoreDictResolver {

    /**
     * Элементы хранилища
     */
    NamedList<DictData> getItems();

}
