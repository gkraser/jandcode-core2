package jandcode.core.dbm.genid;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

import java.util.*;

/**
 * Сервис уникальных id.
 */
public interface GenIdService extends Comp, IModelMember {

    /**
     * Зарегистрированные генераторы.
     */
    NamedList<GenId> getGenIds();

    /**
     * Возвращает генератор по имени
     *
     * @param genIdName имя генератора
     * @return ошибка, если генератора нет
     */
    GenId getGenId(String genIdName);

    /**
     * Возвращает генератор с кешем по имени.
     * Такой генератор выделяет id блоками размером cacheSize (в штуках).
     * Это позволяет увеличить скорость массовой вставки данных.
     * <p>
     * Не все драйвера genid могут поддерживать такой функционал,
     * поэтому, если драйвер не поддерживает кеш, то будет возвращает обычный
     * генератор и кеш будет проигнорирован.
     *
     * @param genIdName имя генератора
     * @param cacheSize размер кеша
     * @return генератор
     */
    GenId getGenId(String genIdName, long cacheSize);

    /**
     * Установить для генератора текущее значение
     * (см: {@link GenId#getCurrentId()}.
     *
     * @param genIdName имя генератора
     * @param value     устанавливаемое значение. Возможно реально будет установлено
     *                  другое значение, если генератор имеет шаг, не равный 1.
     */
    void updateCurrentId(String genIdName, long value);

    /**
     * Восстановить состояние всех генераторов в соответствии с текущим положением в базе данных.
     * Используется обычно только в цикле разработки/установки.
     * <p>
     * Ошибки игнорируются и выводятся в Log.
     */
    void recoverGenIds() throws Exception;

    /**
     * Восстановить состояние указанных генераторов в соответствии с текущим положением в базе данных.
     * Используется обычно только в цикле разработки/установки.
     * <p>
     * Подразумевается, что для генератора имеется таблица с именем как у генератора,
     * и в этой таблице имеется поле id, значение которого было сгенерировано генератором.
     *
     * @param genIdNames имена генераторов, которые нужно обработать. Если null, обрабатываются все
     * @param throwError true - генерировать ошибки, если будут. При значении false -
     *                   ошибки просто выводятся в log
     */
    void recoverGenIds(List<String> genIdNames, boolean throwError) throws Exception;


}
