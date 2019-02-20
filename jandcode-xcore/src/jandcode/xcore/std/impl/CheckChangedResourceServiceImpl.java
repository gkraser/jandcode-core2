package jandcode.xcore.std.impl;

import jandcode.xcore.*;
import jandcode.xcore.std.*;

public class CheckChangedResourceServiceImpl extends BaseComp implements CheckChangedResourceService {

    /**
     * Последнее время проверки
     */
    private long lastTime = 0;

    /**
     * Сейчас работает
     */
    private boolean working;

    /**
     * Интервал проверки перезагрузки в ms
     */
    private long reloadInterval = 500;

    // возвращает в случах, когда делать ничего не нужно
    private CheckChangedResourceInfo dummyChangedResourceInfo = new CheckChangedResourceInfoImpl();

    // возвращается после проверки, что бы не пложить левых экземпляров
    private CheckChangedResourceInfoImpl changedResourceInfo = new CheckChangedResourceInfoImpl();

    public void setReloadInterval(long reloadInterval) {
        this.reloadInterval = reloadInterval;
    }

    public long getReloadInterval() {
        return reloadInterval;
    }

    public CheckChangedResourceInfo checkChangedResource() throws Exception {
        boolean res = false;
        if (working) {
            return dummyChangedResourceInfo; // уже работает
        }
        working = true;
        long n = System.currentTimeMillis() - lastTime;
        try {
            if (n > reloadInterval) {
                changedResourceInfo.clear();
                try {
                    // проверяем
                    for (ICheckChangedResource r : getApp().impl(ICheckChangedResource.class)) {
                        r.checkChangedResource(changedResourceInfo);
                    }
                } finally {
                    lastTime = System.currentTimeMillis();
                }
                return changedResourceInfo;
            } else {
                return dummyChangedResourceInfo;
            }
        } finally {
            working = false;
        }
    }

}
