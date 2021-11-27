package jandcode.commons.reflect.impl;

import jandcode.commons.reflect.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class BaseReflectProps implements IReflectProps {

    private static List<String> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>());

    protected Map<String, Object> props;

    public void setProp(String name, Object value) {
        if (value == null) {
            if (props != null) {
                props.remove(name);
            }
        } else {
            if (props == null) {
                synchronized (this) {
                    if (props == null) {
                        props = new ConcurrentHashMap<>();
                    }
                }
            }
            props.put(name, value);
        }
    }

    public Object getProp(String name) {
        if (props == null) {
            return null;
        }
        return props.get(name);
    }

    public Collection<String> getPropNames() {
        if (props == null) {
            return EMPTY_LIST;
        }
        return props.keySet();
    }

}
