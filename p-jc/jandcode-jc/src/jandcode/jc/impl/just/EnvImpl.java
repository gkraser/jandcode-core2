package jandcode.jc.impl.just;

import jandcode.jc.*;

public class EnvImpl implements Env {

    private Ctx ctx;
    private Boolean prod;

    public EnvImpl(Ctx ctx) {
        this.ctx = ctx;
    }

    public boolean isProd() {
        return prod == null ? false : prod;
    }

    public void setProd(boolean v) {
        if (this.prod == null) {
            this.prod = v;
        } else if (v != this.prod) {
            this.prod = v;
            ctx.warn("set env.prod=" + v);
        }
    }

}
