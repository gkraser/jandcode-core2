package jandcode.core.db;

import jandcode.commons.error.*;
import jandcode.core.*;

/**
 * Предок для компонентов связанных с DbSource
 */
public abstract class BaseDbSourceMember extends BaseComp implements IDbSourceMember {

    private DbSource dbSource;

    public DbSource getDbSource() {
        return dbSource;
    }

    public void setDbSource(DbSource dbSource) {
        if (this.dbSource != null) {
            throw new XError("dbSource нельзя сменить");
        }
        this.dbSource = dbSource;
    }

}
