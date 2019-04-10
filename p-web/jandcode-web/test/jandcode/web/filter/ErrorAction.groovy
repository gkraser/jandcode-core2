package jandcode.web.filter

import jandcode.commons.error.*
import jandcode.web.action.*

class ErrorAction extends BaseAction {

    protected void onExec() throws Exception {
        throw new XError("error")
    }
}
