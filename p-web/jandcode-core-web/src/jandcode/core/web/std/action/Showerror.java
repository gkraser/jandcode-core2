package jandcode.core.web.std.action;

import jandcode.commons.*;
import jandcode.core.web.action.*;
import jandcode.core.web.gsp.*;

public class Showerror extends BaseAction {

    protected void onExec() throws Exception {

        ShowerrorInfo info = new ShowerrorInfo(getReq());

        String tml = info.isAjax() ? "showerror-ajax" : "showerror";

        getReq().render(new GspTemplate(tml, UtCnv.toMap(
                "info", info
        )));
    }

}
