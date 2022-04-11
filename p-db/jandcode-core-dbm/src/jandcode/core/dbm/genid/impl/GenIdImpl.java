package jandcode.core.dbm.genid.impl;

import jandcode.commons.error.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.genid.*;

/**
 * Основная и единственная реализация GenId.
 */
public class GenIdImpl extends BaseModelMember implements GenId {

    private long start = 1000;
    private long step = 1;
    private GenIdDriver driver;

    public GenIdImpl(GenIdDriver driver, String name, long start, long step) {
        this.driver = driver;
        setApp(driver.getApp());
        setModel(driver.getModel());
        setName(name);
        //
        if (start > 0) {
            this.start = start;
        }
        if (step > 0) {
            this.step = step;
        }
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    //////

    protected GenIdDriver getDriver() {
        return driver;
    }

    //////

    public long getNextId() {
        try {
            return getDriver().getNextId(this);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public long getCurrentId() {
        try {
            return getDriver().getCurrentId(this);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    //////

}
