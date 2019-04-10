package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;

/**
 * Системный плагин. Подключается всегда и первым.
 */
public class SystemConfLoaderPlugin extends BaseConfLoaderPlugin {

    public String getVar(String var) throws Exception {
        if (var.equals("path")) {
            return getVar_path();

        } else if (var.startsWith("pathup:")) { //NON-NLS
            return getVar_pathup(UtString.removePrefix(var, "pathup:")); //NON-NLS

        } else if (var.startsWith("pathprop:")) { //NON-NLS
            return getVar_pathprop(UtString.removePrefix(var, "pathprop:")); //NON-NLS

        } else {
            return System.getProperty(var);
        }
    }

    public boolean execFunc(String funcName, Conf params, Conf context) throws Exception {
        if ("include".equals(funcName)) { //NON-NLS
            execFunc_include(params, context);
            return true;
        } else {
            return false;
        }
    }

    ////// funcs

    private void execFunc_include(Conf params, Conf context) throws Exception {
        String path = params.getString("path");
        if (UtString.empty(path)) {
            throw new RuntimeException("function [include] not have value in property [path]");
        }
        boolean required = params.getBoolean("required", true); //NON-NLS
        getLoader().includePath(context, path, required);
    }

    ////// vars

    private String getVar_path() {
        return getLoader().getAbsPath("");
    }

    private String getVar_pathup(String s) {
        String p = UtFile.vfsPathToLocalPath(getLoader().getAbsPath(""));
        String f = UtFile.findFileUp(s, p);
        if (f == null) {
            return p;
        } else {
            return UtFile.path(f);
        }
    }

    private String getVar_pathprop(String s) {
        String p = UtFile.vfsPathToLocalPath(getLoader().getAbsPath(""));
        return UtFile.getPathprop(s, p);
    }

}