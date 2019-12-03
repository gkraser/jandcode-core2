package jandcode.core.web.action;

import java.lang.reflect.*;

public class ActionMethodWrapper1 implements ActionMethodWrapper {

    String prefix = "??";
    String data;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void execActionMethod(Object inst, Method method) throws Exception {
        method.invoke(inst, this);
        String s = prefix + data + "}}";
        ((BaseAction) inst).getReq().render(s);
    }
}
