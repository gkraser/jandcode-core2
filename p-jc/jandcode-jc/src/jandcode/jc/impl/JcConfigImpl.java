package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;

import java.util.*;

public class JcConfigImpl implements JcConfig {

    private String appdir;
    private List<String> autoLoadProjects;
    private boolean runAsProduct;

    //////

    public String getAppdir() {
        if (this.appdir == null) {
            this.appdir = resolveAppdir();
        }
        return this.appdir;
    }

    protected String resolveAppdir() {
        String s = System.getProperty(JcConsts.PROP_APP_DIR);
        if (!UtString.empty(s)) {
            return s;
        }

        throw new XError("Не удалось определить каталог с jandcode-jc. Его местоположение " +
                "должно быть указано в системной переменной [{0}]",
                JcConsts.PROP_APP_DIR);

    }

    public void setAppdir(String path) {
        if (path != null) {
            path = UtFile.abs(path);
        }
        this.appdir = path;
    }

    public List<String> getAutoLoadProjects() {
        if (this.autoLoadProjects == null) {
            // обрабатываем JC_PATH
            List<String> tmp = new ArrayList<>();
            String jcPathValue = System.getenv(JcConsts.ENV_JC_PATH);
            if (!UtString.empty(jcPathValue)) {
                String delim = UtFile.isWindows() ? ";" : ":";
                String[] ar = jcPathValue.split(delim);
                for (String pp : ar) {
                    String pp1 = pp.trim();
                    if (!UtString.empty(pp1)) {
                        tmp.add(pp1);
                    }
                }
            }
            this.autoLoadProjects = tmp;
        }
        return this.autoLoadProjects;
    }

    public boolean isRunAsProduct() {
        return runAsProduct;
    }

    public void setRunAsProduct(boolean runAsProduct) {
        this.runAsProduct = runAsProduct;
    }

}
