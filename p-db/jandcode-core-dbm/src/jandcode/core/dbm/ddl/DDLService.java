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

    /**
     * Создать новый экземпляр скрипта
     */
    DDLScript createScript();

    /**
     * Собрать ddl-скрипт из модели.
     */
    DDLScript grabScript();

}
