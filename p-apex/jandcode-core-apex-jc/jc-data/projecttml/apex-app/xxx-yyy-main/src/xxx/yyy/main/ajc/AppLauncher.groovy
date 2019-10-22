package xxx.yyy.main.ajc

import jandcode.core.apex.ajc.*

class AppLauncher extends ApexJcScript {

    static void main(String[] args) {
        ApexLauncher.launch(args, AppLauncher)
    }

    protected void onInclude() throws Exception {
        include(ApexDefaultCmds)
    }

}
