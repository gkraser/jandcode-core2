package jandcode.core.web.action;

import java.lang.reflect.*;

public class ActionMethodExecutor1 implements ActionMethodExecutor {

    String prefix = "??";
    String data;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void execActionMethod(BaseAction action, Method method) throws Exception {
        method.invoke(action, this);
        String s = prefix + data + "}}";
        ((BaseAction) action).getReq().render(s);
    }
}
