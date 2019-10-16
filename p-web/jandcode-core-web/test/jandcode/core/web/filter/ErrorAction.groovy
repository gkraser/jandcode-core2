package jandcode.core.web.filter

import jandcode.commons.error.*
import jandcode.core.web.action.*

class ErrorAction extends BaseAction {

    protected void onExec() throws Exception {
        throw new XError("error")
    }
}
