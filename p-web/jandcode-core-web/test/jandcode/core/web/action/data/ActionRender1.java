package jandcode.core.web.action.data;

import jandcode.core.web.action.*;

import java.util.*;

public class ActionRender1 extends BaseAction {

    public void map1() throws Exception {
        Map a = new LinkedHashMap();
        a.put("a", "b");
        getReq().render(a);
    }

    public void list1() throws Exception {
        List a = new ArrayList();
        a.add("a");
        a.add("b");
        getReq().render(a);
    }


}
