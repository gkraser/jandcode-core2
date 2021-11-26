package jandcode.core.apx.store;

import jandcode.core.store.*;

/**
 * Утилиты для store
 */
public class ApxStoreUtils {

    /**
     * Установить информацию о пагинации в store
     *
     * @param store    для какого store
     * @param paginate информация о пагинации
     */
    public static void setPaginate(Store store, Paginate paginate) {
        store.setCustomProp(ApxStoreConsts.paginate, paginate);
    }

    /**
     * Получить информацию о пагинации из store
     *
     * @param store для какого store
     * @return информация о пагинации, может быть null
     */
    public static Paginate getPaginate(Store store) {
        return (Paginate) store.getCustomProp(ApxStoreConsts.paginate);
    }

}
