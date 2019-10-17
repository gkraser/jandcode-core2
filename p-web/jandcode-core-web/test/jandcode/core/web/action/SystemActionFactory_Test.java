package jandcode.core.web.action;

import jandcode.core.web.*;
import jandcode.core.web.action.impl.*;
import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SystemActionFactory_Test extends Web_Test {

    private Request check(String uri, String actionName) {
        Request r = web.createRequest(uri);
        SystemActionFactory f = app.create(SystemActionFactory.class);

        BaseAction a = (BaseAction) f.createAction(r);

        String an = null;
        if (a != null) {
            an = a.getName();
        }

        assertEquals(actionName, an);

        r.setAction(a);
        return r;
    }

    @Test
    public void test1() throws Exception {
        check("z1", "z1");
        check("__z1", null);
        check("z1/b", "z1");
        check("z1/a", "z1");
        check("z1/a/b", "z1/a/b");
        check("Z1/A/b", "z1/a/b");
    }

    @Test
    public void test2() throws Exception {
        Request r;
        //
        r = check("z1/a/b/c/d/F/g", "z1/a/b");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionMethod), "c");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionMethodPathInfo), "d/F/g");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionPathInfo), "c/d/F/g");

        //
        r = check("z1/a/b/c", "z1/a/b");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionMethod), "c");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionMethodPathInfo), "");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionPathInfo), "c");

        //
        r = check("z1/a/b", "z1/a/b");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionMethod), "");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionMethodPathInfo), "");
        assertEquals(r.getAttrs().getString(WebConsts.a_actionPathInfo), "");


    }

}
