package jandcode.commons.variant;

import java.util.*;

/**
 * Типизированная map
 */
public class VariantMap extends LinkedHashMap<String, Object> implements IVariantMap {

    public VariantMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public VariantMap(int initialCapacity) {
        super(initialCapacity);
    }

    public VariantMap() {
    }

    public VariantMap(Map<? extends String, ?> m) {
        super(m);
    }

    public VariantMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    //////

    public Object getValue(String name) {
        return get(name);
    }

    public void setValue(String name, Object value) {
        put(name, value);
    }

    public Object get(String key, Object defValue) {
        Object v = get(key);
        if (v == null) {
            return defValue;
        }
        return v;
    }

}
