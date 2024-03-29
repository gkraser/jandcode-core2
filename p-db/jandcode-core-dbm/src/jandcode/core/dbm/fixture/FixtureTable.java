package jandcode.core.dbm.fixture;


import jandcode.commons.named.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Таблица фикстуры.
 * Имя соответствует имени таблицы в базе и имени Store.
 */
public interface FixtureTable extends INamed, IFixtureGenId {

    /**
     * Какой фикстуре принадлежит
     */
    Fixture getFixture();

    /**
     * Store с данными таблицы.
     */
    Store getStore();

    /**
     * Добавить запись.
     * Если запись не содержит id, она будет сгенерирована.
     * id должна быть в диапазоне rangeId
     *
     * @param data данные записи
     * @return добавленная запись
     */
    StoreRecord add(Map data);

    /**
     * Загрузить из файла. Используется StoreLoader, создаваемый по расширению
     * файла (например csv, xml)
     *
     * @param fileName ия файла в формате vfs
     */
    void loadFromFile(String fileName) throws Exception;

    /**
     * Получить диапазон задействованных id для таблицы.
     */
    FixtureRangeId getRangeId();

    /**
     * Возвращает максимальную id в рамках диапазона.
     * <p>
     * Если для таблицы указан rangeId, то возвращается максимальная id из
     * данных в этом диапазоне.
     * <p>
     * Если rangeId не указан, то возвращается максимальный id из данных.
     */
    long getMaxIdInRange();

}
