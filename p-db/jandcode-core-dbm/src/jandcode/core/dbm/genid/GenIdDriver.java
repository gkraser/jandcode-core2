package jandcode.core.dbm.genid;


import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Драйвер реализации genid в базе данных.
 */
public interface GenIdDriver extends Comp, IModelMember, IConfLink {

    /**
     * Возвращает следующее уникальное значение
     *
     * @param genId для какого генератора
     */
    long getNextId(GenId genId) throws Exception;

    /**
     * Текущее id. Это id уже выдано генератором.
     * Не факт, что именно вызывающему.
     *
     * @param genId для какого генератора
     */
    long getCurrentId(GenId genId) throws Exception;

    /**
     * Возвращает true, если поддерживается метод
     * {@link GenIdDriver#updateCurrentId(GenId, long)}.
     *
     * @param genId для какого генератора
     */
    boolean isSupportUpdateCurrentId(GenId genId);

    /**
     * Установить для генератора текущее значение
     * (см: {@link GenId#getCurrentId()}.
     * Это значение будет считаться "выданным".
     * <p>
     * Если метод не поддерживается, то он не должен генерировать ошибку.
     *
     * @param value устанавливаемое значение. Возможно реально будет установлено
     *              другое значение, если генератор имеет шаг, не равный 1.
     */
    void updateCurrentId(GenId genId, long value) throws Exception;

    /**
     * Возвращает true, если поддерживается метод
     * {@link GenIdDriver#getGenIdCache(GenId, long)}.
     *
     * @param genId для какого генератора
     */
    boolean isSupportGenIdCache(GenId genId);

    /**
     * Возвращает кеш genid с указанным количеством элементов.
     * <p>
     * Если метод не поддерживается, должна быть сгенерирована ошибка.
     *
     * @param genId для какого генератора
     * @param count сколько значений требуется.
     */
    GenIdCache getGenIdCache(GenId genId, long count) throws Exception;

}

