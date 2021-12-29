package jandcode.core.store;

import jandcode.commons.io.*;
import jandcode.core.*;

/**
 * Загрузчик данных в {@link Store}.
 */
public interface StoreLoader extends Comp, ILoader {

    /**
     * Установить store, в который нужно загружать данные.
     * store не будет очищатся перед загрузкой.
     */
    void setStore(Store store);

    /**
     * Загруженный store. Если был явно установлен
     * методом {@link StoreLoader#setStore(Store)},
     * то возвращается он. Иначе возвращается экземпляр, созданный в процессе загрузки.
     */
    Store getStore();

}
