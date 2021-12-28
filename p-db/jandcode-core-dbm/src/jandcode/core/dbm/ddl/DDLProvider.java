package jandcode.core.dbm.ddl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

import java.util.*;

/**
 * Провайдер ddl для модели.
 * <p>
 * По модели разбросаны теги ddl.
 * Для каждого создается такой объект, из которого модель и собирает
 * свою структуру.
 * <p>
 * Экземпляр создается для каждого процесса сбора ddl. Если один и тот же
 * провайдер работает на нескольких стадиях, то экземпляр будет переиспользоватся.
 */
public interface DDLProvider extends Comp, IConfLink, IModelMember, IDDLProvider {

    /**
     * Для каких стадий предназначен (см: {@link DDLStage}).
     * По умолчанию: [afterTables]
     */
    Set<DDLStage> getStages();

}
