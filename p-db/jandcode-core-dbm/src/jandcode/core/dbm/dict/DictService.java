package jandcode.core.dbm.dict;

import jandcode.core.*;
import jandcode.core.dbm.*;

import java.util.*;

/**
 * Сервис словарей
 */
public interface DictService extends Comp, IModelMember, IDictService {

    /**
     * По переданному набору ids вернет данные для указанного словаря.
     */
    DictData resolveIds(Dict dict, Collection<Object> ids) throws Exception;

    /**
     * Кеш данных словарей
     */
    DictCache getCache();

}
