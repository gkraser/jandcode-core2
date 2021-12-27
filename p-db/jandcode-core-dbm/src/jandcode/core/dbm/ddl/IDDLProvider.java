package jandcode.core.dbm.ddl;

import java.util.*;

/**
 * Провайдер dll операторов
 */
public interface IDDLProvider {

    /**
     * Получить набор ddl операторов для указаной стадии.
     *
     * @param stage стадия
     * @return список операций или null (или пустой список), если нет операций
     */
    List<DDLOper> loadOpers(DDLStage stage) throws Exception;


}
