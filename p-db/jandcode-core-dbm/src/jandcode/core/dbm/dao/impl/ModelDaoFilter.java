package jandcode.core.dbm.dao.impl;

import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.impl.*;

/**
 * dao-filter для модели.
 * Включает в контекст dao Model и Db.
 * Db устанавливает соединенение по требованию. Т.е.
 * если в dao небыло операций с базой данных, соединение не будет установлено.
 * После отработки dao - отсоединяет Db.
 */
public class ModelDaoFilter extends BaseModelMember implements DaoFilter {

    public void before(DaoContext ctx) throws Exception {
        Db db = getModel().createDb();
        Db dbw = new ModelDbWrapper(db, true, true);
        //
        ctx.getBeanFactory().registerBean(Db.class.getName(), dbw);
        ctx.getBeanFactory().registerBean(Model.class.getName(), getModel());
    }

    public void after(DaoContext ctx) throws Exception {
        Db db = ctx.bean(Db.class);
        if (db.isConnected()) {
            // кто то попользовался
            if (db.isTran()) {
                db.commit();
            }
            db.disconnectForce();
        }
    }

    public void cleanup(DaoContext ctx) throws Exception {
        Db db = ctx.bean(Db.class);
        if (db.isConnected()) {
            // кто то попользовался и не закрыл
            // например в after не сработал commit
            try {
                if (db.isTran()) {
                    db.rollback();
                }
            } finally {
                db.disconnectForce();
            }
        }
    }

    //////

    public void execDaoFilter(DaoFilterType type, DaoContext ctx) throws Exception {
        if (type == DaoFilterType.before) {
            before(ctx);
        } else if (type == DaoFilterType.after) {
            after(ctx);
        } else if (type == DaoFilterType.cleanup) {
            cleanup(ctx);
        }
    }

}
