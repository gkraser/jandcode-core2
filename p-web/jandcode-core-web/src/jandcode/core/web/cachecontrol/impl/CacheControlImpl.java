package jandcode.core.web.cachecontrol.impl;

import jandcode.core.*;
import jandcode.core.web.cachecontrol.*;

public class CacheControlImpl extends BaseComp implements CacheControl {

    private String mask;
    private String cacheControl;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

}
