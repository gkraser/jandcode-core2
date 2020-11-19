package jandcode.core.dbm.dict;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Словарь
 */
public interface Dict extends Comp, IConfLink, IModelMember {

    /**
     * Домен со структурой словаря
     */
    Domain getDomain();

    /**
     * Объект, который реализаует определенные интерфейсы для обработки данных
     * словаря.
     */
    DictHandler getHandler();

    /**
     * Имя поля по умолчанию
     */
    String getDefaultField();

    /**
     * Создать пустой store со структурой словаря
     */
    Store createStore();

    /**
     * Создать пустой DictData для словаря
     */
    DictData createDictData();

    /**
     * По переданному набору ids вернет данные словаря.
     */
    DictData resolveIds(Collection ids) throws Exception;

}
