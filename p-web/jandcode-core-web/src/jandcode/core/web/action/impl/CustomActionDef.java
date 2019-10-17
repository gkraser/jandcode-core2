package jandcode.core.web.action.impl;

import jandcode.core.*;
import jandcode.core.web.action.*;

public abstract class CustomActionDef extends BaseComp implements ActionDef, Comparable<ActionDef> {

    private int countFacet = -1;

    public int getCountFacet() {
        if (countFacet == -1) {
            countFacet = getName().split("/").length;
        }
        return countFacet;
    }

    public int compareTo(ActionDef o) {
        Integer len1 = o.getCountFacet();
        int len2 = getCountFacet();
        int res = len1.compareTo(len2);
        if (res != 0) {
            return res;
        }
        return getName().compareToIgnoreCase(o.getName());
    }

}
