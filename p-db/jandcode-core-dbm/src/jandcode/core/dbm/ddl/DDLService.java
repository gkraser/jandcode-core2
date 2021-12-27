package jandcode.core.dbm.ddl;

import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Сервис для работы с ddl
 */
public interface DDLService extends Comp, IModelMember {

    /**
     * Создать новый экземпляр оператора
     */
    DDLOper createOperInst(String type);


}
