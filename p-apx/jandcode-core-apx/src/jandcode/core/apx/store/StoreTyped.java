package jandcode.core.apx.store;

import jandcode.core.store.*;
import jandcode.core.store.std.*;

/**
 * "Типизированный" враппер для store.
 * Такие классы используются, что бы возвращать значения из dao с указанием типа
 * записи, что позволит знать структуру возвращаемого store.
 * <p>
 * Сам тип T никак не используется.
 * <p>
 * Пример использования:
 * <pre>{@code
 * StoreTyped<RecordStruct> store2 = new StoreTyped<>(store)
 * }</pre>
 *
 * @param <T>
 */
public class StoreTyped<T> extends BaseStoreWrapper {

    public StoreTyped(Store store) {
        super(store);
    }

}
