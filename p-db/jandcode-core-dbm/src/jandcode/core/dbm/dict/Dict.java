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
     * Class dao, с помошью которого можно получать данные словаря
     */
    Class getDaoClass();

    /**
     * Имя поля по умолчанию
     */
    String getDefaultField();

    /**
     * По переданному набору ids вернет store структуры getDomain()
     * с заполненными данными словаря.
     *
     * @param ids набор id
     * @return store с данными словаря для указанных id
     */
    Store resolveIds(Collection ids);

}
