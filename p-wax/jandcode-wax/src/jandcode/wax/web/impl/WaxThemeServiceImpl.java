package jandcode.wax.web.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.wax.web.*;

import java.util.*;
import java.util.regex.*;

public class WaxThemeServiceImpl extends BaseComp implements WaxThemeService {

    private static Pattern patternTheme = Pattern.compile(".*/css/theme-(.*).js");

    private Map<String, String> themes;

    public Collection<String> getThemeNames() {
        return getThemes().keySet();
    }

    public String getDefaultThemeName() {
        return "std";
    }

    public String getThemeFile(String name) {
        String res = getThemes().get(name);
        if (res == null) {
            res = getThemes().get(getDefaultThemeName());
            if (res == null) {
                throw new XError("Не найдена тема {0}", name);
            }
        }
        return res;
    }

    //////

    protected Map<String, String> getThemes() {
        if (themes == null) {
            synchronized (this) {
                if (themes == null) {
                    themes = loadThemes();
                }
            }
        }
        return themes;
    }

    protected Map<String, String> loadThemes() {
        Map<String, String> res = new HashMap<>();
        List<String> lst = UtWeb.expandPath(getApp(), "[*]/css/theme-*.js");
        for (String f : lst) {
            Matcher m = patternTheme.matcher(f);
            if (m.find()) {
                res.put(m.group(1), f);
            }
        }
        return res;
    }

}
