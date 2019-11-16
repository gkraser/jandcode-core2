package jandcode.core.jsa.theme.impl;

import jandcode.core.*;
import jandcode.core.jsa.theme.*;
import jandcode.core.web.*;

import java.util.*;
import java.util.regex.*;

public class JsaThemeServiceImpl extends BaseComp implements JsaThemeService {

    private static Pattern patternTheme = Pattern.compile(".*/css/(.*)-theme.js");

    private Map<String, String> themes;

    public Collection<String> getThemeNames() {
        return getThemes().keySet();
    }

    public String findThemeFile(String name) {
        return getThemes().get(name);
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
        List<String> lst = UtWeb.expandPath(getApp(), "[*]/css/*-theme.js");
        for (String f : lst) {
            Matcher m = patternTheme.matcher(f);
            if (m.find()) {
                res.put(m.group(1), f);
            }
        }
        return res;
    }

}
