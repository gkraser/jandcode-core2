package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.str.*;
import jandcode.commons.variant.*;
import jandcode.db.*;

import java.util.*;

public class DbSourcePropsImpl extends VariantMap implements DbSourceProps {

    private PropsExpander propsExp;

    class PropsExpander implements ISubstVar {

        private Set<String> stack = new HashSet<>();

        public String expandValue(Object value) {
            if (value == null) {
                return "";
            }
            return UtString.substVar(UtString.toString(value), this);
        }

        public String expandProp(String name) {
            if (stack.contains(name)) {
                return "";
            }
            stack.add(name);
            try {
                return expandValue(getRaw(name));
            } finally {
                stack.remove(name);
            }
        }

        public String onSubstVar(String v) {
            return expandProp(v);
        }

    }

    public DbSourcePropsImpl() {
        propsExp = new PropsExpander();
    }

    public Object getRaw(String key) {
        return super.get(key);
    }

    public Object get(Object key) {
        return propsExp.expandProp(UtString.toString(key));
    }

    public IVariantMap subMap(String prefix, boolean raw) {
        prefix = prefix + ".";
        IVariantMap res = new VariantMap();
        for (String key : keySet()) {
            String k2 = UtString.removePrefix(key, prefix);
            if (k2 != null) {
                Object v;
                if (raw) {
                    v = getRaw(key);
                } else {
                    v = get(key);
                }
                res.put(k2, v);
            }
        }
        return res;
    }
}
