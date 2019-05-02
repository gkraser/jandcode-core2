package jandcode.db.base;

import jandcode.commons.error.*;
import jandcode.db.*;

/**
 * Предок
 */
public class BaseDbManagerService extends BaseDbSourceMember implements DbManagerService {

    private DbSource systemDbSource;

    /**
     * Системная база данных. Определяется как клон основной, где
     * свойства заменены свойствами с префиксом 'system.'
     */
    public DbSource getSystemDbSource() {
        if (systemDbSource == null) {
            systemDbSource = getDbSource().cloneComp();
            systemDbSource.setProps(getDbSource().getProps("system", true, true));
        }
        return systemDbSource;
    }

    public boolean existDatabase() throws Exception {
        throw new XError("Not implemented");
    }

    public void createDatabase() throws Exception {
        throw new XError("Not implemented");
    }

    public void dropDatabase() throws Exception {
        throw new XError("Not implemented");
    }

}
