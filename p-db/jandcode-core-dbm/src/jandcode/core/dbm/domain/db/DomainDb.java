package jandcode.core.dbm.domain.db;

import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

/**
 * Представление домена как таблицы в базе данных
 */
public interface DomainDb extends Comp, IModelMember, IDomainMember {

    /**
     * Является ли таблицей в базе данных
     */
    boolean isDbTable();

    /**
     * Индексы для домена
     */
    List<DomainDbIndex> getIndexes();

    /**
     * Старовое значение для генератора создаваемого для домена.
     */
    long getGenIdStart();

    /**
     * Шаг приращения для генератора создаваемого для домена.
     */
    long getGenIdStep();

}
