package jandcode.core.web.gsp.data

import jandcode.core.web.gsp.*

class Gsp1 extends BaseGsp {

    protected void onRender() throws Exception {
        out("Gsp1 [${name}], args: ${args}")
    }

}
