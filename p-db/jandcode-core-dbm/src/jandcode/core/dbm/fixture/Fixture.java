package jandcode.core.dbm.fixture;

import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.fixture.impl.*;
import jandcode.core.store.*;

/**
 * Фикстура.
 * Это набор store с данными, которые представляют собой
 * подмножество данных, которые можно записать в базу данных
 * и использовать в качестве тестовых данных.
 */
public interface Fixture extends INamed, INamedSet, IModelLink {

    /**
     * Создать экземпляр
     */
    static Fixture create(Model model) {
        return new FixtureImpl(model);
    }

    /**
     * Таблицы, входящие в набор.
     * <p>
     * Только для чтения!
     */
    NamedList<FixtureTable> getTables();

    /**
     * Store, входящие в набор.
     * <p>
     * Только для чтения!
     */
    NamedList<Store> getStores();

    /**
     * Таблица по имени.
     * Если ее еще нет - создается.
     *
     * @param name имя таблицы (=имя домена)
     */
    FixtureTable table(String name);

}
