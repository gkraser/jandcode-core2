package jandcode.core.web.action.data;

import jandcode.core.web.action.*;

public class ActionMethod1 extends BaseAction {

    public void m1() throws Exception {
        getReq().render("m1-render");
    }

    public String m2() throws Exception {
        return "m2-render";
    }

    public void m3(ActionMethodExecutor1 wrap) throws Exception {
        wrap.setPrefix("**");
        wrap.setData("m3-render");
    }

}
