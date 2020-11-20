package jandcode.core.dbm.dict;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

import java.util.*;

public interface DictService extends Comp, IModelMember {

    /**
     * Все зарегистрированные словари.
     * Только для чтения.
     */
    NamedList<Dict> getDicts();

    /**
     * По переданному набору ids вернет данные для указанного словаря.
     */
    DictData resolveIds(Dict dict, Collection<Object> ids) throws Exception;

    /**
     * Для объектов, которые поддерживают связь со словарями (например store),
     * загружает необходимые словарные данные.
     * <p>
     * Для неизвестных типов объектов ничего не делает.
     *
     * @param data объект с данными, например Store
     */
    void resolveDicts(Object data) throws Exception;

    /**
     * Кеш данных словарей
     */
    DictCache getCache();

}
