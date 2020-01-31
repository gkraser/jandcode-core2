package xxx.yyy.main;

import jandcode.commons.*;
import jandcode.core.*;

public class AppLauncher {

    public static void main(String[] args) throws Exception {
        new AppLauncher().run(args);
    }

    public void run(String[] args) throws Exception {
        UtLog.logOff();
        App app = AppLoader.load(AppConsts.FILE_APP_CONF);
        System.out.println("app loaded: " + app.getAppConfFile());
    }

}
