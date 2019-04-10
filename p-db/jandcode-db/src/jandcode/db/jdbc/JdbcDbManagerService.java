package jandcode.db.jdbc;

import jandcode.db.*;
import jandcode.commons.error.*;

/**
 * Предок
 */
public class JdbcDbManagerService extends BaseDbSourceMember implements DbManagerService {

    protected Db _systemDb;

    /**
     * Системная база данных. Определяется как клон основной, где
     * свойства заменены свойствами с префиксом 'system.'
     */
    protected Db getSystemDb() {
        if (_systemDb == null) {
            DbSource dbsSys = getDbSource().cloneComp();
            dbsSys.getProps().putAll(getDbSource().getProps("system", true));
            _systemDb = dbsSys.createDb();
        }
        return _systemDb;
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
