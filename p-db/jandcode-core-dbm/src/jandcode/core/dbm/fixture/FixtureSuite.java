package jandcode.core.dbm.fixture;

import jandcode.commons.conf.*;
import jandcode.core.*;

import java.util.*;

/**
 * Набор {@link FixtureBuilder}
 */
public interface FixtureSuite extends Comp, IConfLink {

    /**
     * Создать построители, входящие в этот набор
     */
    List<FixtureBuilder> createBuilders();

}
