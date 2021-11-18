package jandcode.core.web.undertow;

import jandcode.core.web.action.*;

public class A1Action extends BaseAction {

    public void m1() {
        getReq().render("hello a1/m1");
    }

}
