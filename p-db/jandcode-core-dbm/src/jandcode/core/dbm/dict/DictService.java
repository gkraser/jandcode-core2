package jandcode.core.dbm.dict;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

public interface DictService extends Comp, IModelMember {

    /**
     * Все зарегистрированные словари.
     * Только для чтения.
     */
    NamedList<Dict> getDicts();

}
