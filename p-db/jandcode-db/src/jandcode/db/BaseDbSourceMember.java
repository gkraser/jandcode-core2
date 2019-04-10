package jandcode.db;

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
        this.dbSource = dbSource;
    }

}
