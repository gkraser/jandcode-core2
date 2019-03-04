package core._inc;

import jandcode.core.*;

public class ModuleApi extends BaseComp {

    public void test_createSubHolder() throws Exception {
        //= createSubHolder
        ModuleSubHolder h = getApp().getModules().createSubHolder();
        h.add("jandcode-web");
        h.add("jandcode-db-derby");
        for (Module m : h) {
            System.out.println(m.getName());
        }
        //=
    }


}
