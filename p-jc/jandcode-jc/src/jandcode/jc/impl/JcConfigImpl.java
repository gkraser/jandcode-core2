package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.jc.*;
import jandcode.jc.impl.lib.*;

import java.text.*;
import java.util.*;

public class JcConfigImpl implements JcConfig {

    private Conf conf = UtConf.create();
    private String appdir;
    private List<String> autoLoadProjects;

    public Conf getConf() {
        return conf;
    }

    //////

    public void load(String workdir) throws Exception {
        // чистим
        this.conf.clear();

        // загружаем конфиг по умолчанию
        UtConf.load(this.conf).fromRes("res:jandcode/jc/jc-cfg-default.cfx");

        // загружаем из каталога пользователя
        String fn = UtFile.getHomedir() + "/.config/jandcode/" + JcConsts.CONFIG_FILE;
        joinFile(fn);
    }

    protected void joinFile(String fn) {
        if (UtString.empty(fn)) {
            return;
        }
        if (!UtFile.exists(fn)) {
            return;
        }
        try {
            Conf r = UtConf.create();
            UtConf.load(r).fromFile(fn);
            this.conf.join(r);
        } catch (Exception e) {
            System.out.println(MessageFormat.format("WARNING! Ошибка при загрузке конфигурации {0}", fn));
            System.out.println(UtError.createErrorInfo(e).getText());
        }
    }

    public String getAppdir() {
        if (this.appdir == null) {
            this.appdir = resolveAppdir();
        }
        return this.appdir;
    }

    protected String resolveAppdir() {

        // из свойства
        String s = System.getProperty(JcConsts.PROP_APP_DIR);
        if (!UtString.empty(s)) {
            return s;
        }

        // по местоположению jar
        String coreJarFile = LibUtils.findJarFileByClassname("jandcode.jc.Project");
        if (coreJarFile != null) {
            String corePath = UtFile.path(coreJarFile);
            String dn = UtFile.filename(corePath);
            if ("lib".equals(dn)) {
                // jar лежит в каталоге lib и этого достаточно
                return UtFile.abs(corePath + "/..");
            }
        }

        // по свойству .pathprop
        String root1 = UtFile.getPathprop(UtilsConsts.PATHPROP_COREROOT, "");
        if (!UtString.empty(root1)) {
            root1 = UtFile.abs(root1);
            if (UtFile.exists(root1)) {
                return root1;
            }
        }

        throw new XError("Не удалось определить каталог с jandcode-jc. Его местоположение " +
                "должно быть указано либо в системной переменной [{0}], либо в переменной pathprop [{1}], " +
                "либо файл jandcode-jc.jar должен быть расположен в каталоге lib бинарного дистрибутива.",
                JcConsts.PROP_APP_DIR, UtilsConsts.PATHPROP_COREROOT);

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
}
