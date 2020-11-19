package jandcode.core.dbm.dict;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

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

}
