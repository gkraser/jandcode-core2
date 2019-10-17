package jandcode.core.store;

/**
 * Методы сервиса store
 */
public interface IStoreService {

    /**
     * Создать экземпляр поля по имени типа
     */
    StoreField createStoreField(String type);

    /**
     * Создать пустой store без полей
     */
    Store createStore();

}
