package jandcode.db.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.db.*;

public class DbDriverDefImpl extends BaseComp implements DbDriverDef {

    private Conf conf;
    private DbDriver inst;

    public DbDriverDefImpl(App app, Conf conf) {
        setApp(app);
        this.conf = conf;
        setName(this.conf.getName());
    }

    public DbDriver createInst() {
        return (DbDriver) getApp().create(conf);
    }

    public DbDriver getInst() {
        if (this.inst == null) {
            synchronized (this) {
                if (this.inst == null) {
                    this.inst = createInst();
                }
            }
        }
        return this.inst;
    }
}
