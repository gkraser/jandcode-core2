package jandcode.core.apx.store;

import jandcode.core.store.*;
import jandcode.core.store.std.*;

/**
 * "Типизированный" враппер для записи store.
 * Такие классы используются, что бы возвращать значения из dao с указанием типа
 * записи, что позволит знать структуру возвращаемой записи store.
 * <p>
 * Сам тип T никак не используется.
 * <p>
 * Пример использования:
 * <pre>{@code
 * StoreRecordTyped<RecordStruct> store2 = new StoreRecordTyped<>(storeRecord)
 * }</pre>
 *
 * @param <T>
 */
public class StoreRecordTyped<T> extends BaseStoreRecordWrapper {

    public StoreRecordTyped(StoreRecord storeRecord) {
        super(storeRecord);
    }

}
