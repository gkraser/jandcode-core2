package jandcode.core.dbm.verdb;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Сервис verdb
 */
public interface VerdbService extends Comp, IModelMember {

    /**
     * Модули verdb, которые присутствуют в модели.
     * Список может быть пустым. В этом случае поддержка verdb не включена
     * для модели.
     */
    NamedList<VerdbModuleDef> getVerdbModules();

}
