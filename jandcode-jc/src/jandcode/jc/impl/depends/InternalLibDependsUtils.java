package jandcode.jc.impl.depends;

import jandcode.jc.*;

public class InternalLibDependsUtils {

    public static String ownToText(Object own) {
        if (own == null) {
            return "UNKNOWN";
        }
        if (own instanceof ProjectScript) {
            ProjectScript ps = (ProjectScript) own;
            return ps.getName() + " (" + ps.getClass().getName() + ")";
        }
        if (own instanceof Project) {
            return ((Project) own).getName();
        }
        if (own instanceof Lib) {
            return ((Lib) own).getName();
        }
        return own.toString();
    }

}
