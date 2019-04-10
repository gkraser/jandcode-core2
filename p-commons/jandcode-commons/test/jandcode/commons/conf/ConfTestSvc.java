package jandcode.commons.conf;

import jandcode.commons.*;
import jandcode.commons.test.*;

import java.util.*;

public class ConfTestSvc extends BaseUtilsTestSvc {

    public void printConf(Conf x) {
        Map m = toMap(x);
        utils.outMap(m);
    }

    public Map toMap(Conf x) {
        return UtConf.toMap(x);
    }

    public Conf toConfWithOrigin(Conf x) {
        Conf m = UtConf.create();
        Conf m_orig = m.findConf("$ORIG$", true);
        String s = x.origin().toString();
        String[] ar = s.split("\n");
        for (String s1 : ar) {
            m_orig.setValue("#", s1);
        }
        for (String pn : x.keySet()) {
            Object v = x.getValue(pn);
            if (v instanceof Conf) {
                m.put(pn, toConfWithOrigin((Conf) v));
            } else {
                m.put(pn, v.toString());
            }
        }
        return m;
    }

}
