package jandcode.core.dbm.fixture;


import jandcode.commons.named.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Таблица фикстуры.
 * Имя соответствует имени таблицы в базе и имени Store.
 */
public interface FixtureTable extends INamed {

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

}
