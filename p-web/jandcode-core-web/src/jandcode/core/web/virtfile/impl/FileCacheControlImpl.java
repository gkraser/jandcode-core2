package jandcode.core.web.virtfile.impl;

import jandcode.core.*;
import jandcode.core.web.virtfile.*;

public class FileCacheControlImpl extends BaseComp implements FileCacheControl {

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
