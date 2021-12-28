package jandcode.core.dbm.dbstruct;

import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

/**
 * Представление домена как таблицы в базе данных
 */
public interface DomainDb extends Comp, IModelMember, IDomainMember {

    /**
     * Является ли таблицей в базе данных.
     * conf: {@code <field tag.db="true"/>}
     */
    boolean isDbTable();

    /**
     * true - домен находится в базе данных, но является внешним, по отношению
     * к модели.
     * conf: {@code <field tag.dbexternal="true"/>}
     */
    boolean isDbExternal();

    /**
     * Индексы для таблицы
     * <p>
     * conf:
     * <pre>{@code
     * <field>
     *     <dbindex name="name" fields="f1,f2" unique="true"/>
     * </field>
     * }</pre>
     */
    List<DomainDbIndex> getIndexes();

    /**
     * Стартовое значение для генератора создаваемого для таблицы.
     * conf: {@code <field genid.start="1000"/>}
     */
    long getGenIdStart();

    /**
     * Шаг приращения для генератора создаваемого для таблицы.
     * conf: {@code <field genid.step="1"/>}
     */
    long getGenIdStep();

}
