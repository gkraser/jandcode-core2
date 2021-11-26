package jandcode.core.store;

import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

/**
 * Хранилище записей данных. Представляет собой список записей.
 * Используется как таблица с данными.
 * <p>
 * Не предназначено для работы в конкурентной среде!
 */
public interface Store extends INamed, INamedSet, Iterable<StoreRecord>,
        IAppLink, ICustomProp,
        IStoreFieldHolder, IStoreFieldBuilder {

    /**
     * Количество записей
     */
    int size();

    /**
     * Получить запись по индексу
     */
    StoreRecord get(int index);

    /**
     * Все записи из store в виде списка. Это ссылка на внутренний список записей,
     * так что манипуляции с ним отражаются на самом store. Например его можно
     * отсортировать.
     */
    List<StoreRecord> getRecords();

    //////

    /**
     * Добавить пустую запись
     */
    StoreRecord add();

    /**
     * Добавить запись, полученную из map
     */
    StoreRecord add(Map values);

    /**
     * Добавить новую запись и инициализировать ее из записи rec
     */
    StoreRecord add(StoreRecord rec);

    /**
     * Добавить новую запись из getters указанного объекта.
     */
    StoreRecord add(Object inst);

    /**
     * Добавить все записи из указанной store
     */
    void add(Store store);

    //////

    /**
     * Удалить все записи
     */
    void clear();

    /**
     * Удалить запись по индексу
     */
    void remove(int index);

    //////

    /**
     * Обработчик для получения значений из словаря
     */
    IStoreDictResolver getDictResolver();

    void setDictResolver(IStoreDictResolver dictResolver);

    //////

    /**
     * Клонировать структуру store.
     *
     * @return новый экземпляр store с идентичной структурой без данных
     */
    Store cloneStore();

    //////

    /**
     * Скопировать данные в указанный store.
     * Копируются все записи.
     * Данные копируются по совпадающим именам полей.
     *
     * @param storeDest куда копировать
     */
    void copyTo(Store storeDest);

    ////// index

    /**
     * Найти запись по значению указанного поля.
     * Для работы метода используется внутренний индекс по уникальным
     * значениям указанного поля. Поэтому, если поле имеет дубли в значениях,
     * будет возвращаена последняя запись с таким значением.
     * <p>
     * Индекс формируется в момент первого вызова метода.
     * <p>
     * После удаления/добавления записей индекс очищается.
     * <p>
     * После изменения значений полей индекс НЕ ОЧИЩАЕТСЯ,
     * поэтому, если вы воспользовались индексом, а затем изменили значение
     * в индексируемом поле, необходимо выполнить метод
     * {@link Store#clearIndex()} для перестройки индекса.
     *
     * @param fieldName имя поля
     * @param key       значение поля
     * @return null, если нет такой записи
     */
    StoreRecord getBy(String fieldName, Object key);

    /**
     * Найти запись по значению поля 'id'.
     * см: {@link Store#getBy(java.lang.String, java.lang.Object)}.
     */
    StoreRecord getById(Object key);

    /**
     * Построить и вернуть индекс по указанному полю.
     * Индекс, который вернется, не будет реагировать на изменения
     * в записях store.
     *
     * @param fieldName поле для индексирования
     * @return построенный индекс
     */
    StoreIndex getIndex(String fieldName);

    /**
     * Очистить все индексы
     */
    void clearIndex();

    ////// sort

    /**
     * Отсортировать
     *
     * @param fields список полей через ','. Если имя поля начинается с '*', то по этому полю
     *               сортируется по убыванию, иначе по возрастанию.
     */
    void sort(String fields);

}
