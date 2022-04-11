package jandcode.core.dbm.genid;

import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Генератор уникальных id.
 * Используется как средство заполнения значения поля id (primary key)
 * в таблицах в базе данных.
 */
public interface GenId extends Comp, IModelMember {

    /**
     * Возвращает следующее уникальное значение
     */
    long getNextId();

    /**
     * Текущее id. Это id уже выдано генератором.
     */
    long getCurrentId();

    /**
     * Стартовая id, с которой генератор был создан изначально.
     * Используется на этапе формирования генераторов в базе данных.
     * По умолчанию: 1000.
     */
    long getStart();

    /**
     * Шаг приращения. По умолчанию - 1.
     * Должен быть >= 1.
     * <p>
     * Следует заметить, что настоящий шаг определяется драйвером genid по данным в базе,
     * и может отличаться от объявленного. Например администратор может для
     * репликационной базы данных установить специальный шаг и стартовое значение.
     */
    long getStep();

}
