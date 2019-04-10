package jandcode.web.gsp.data

import jandcode.web.gsp.*

class Gsp1 extends BaseGsp {

    protected void onRender() throws Exception {
        out("Gsp1 [${name}], args: ${args}")
    }

}
