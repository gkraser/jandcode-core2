package jandcode.core.dbm.fixture;

import jandcode.core.dbm.*;

/**
 * Построитель fixture
 */
public interface FixtureBuilder {

    /**
     * Построить fixture для указанной модели.
     */
    Fixture build(Model model);

}
