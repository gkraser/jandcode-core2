package jandcode.commons.variant;

import java.util.*;

/**
 * MapЕнзув, где ключи регистронезависимые строки
 */
public class VariantMapNoCase extends VariantMap {

    public Object get(Object key) {
        key = ((String) key).toLowerCase();
        return super.get(key);
    }

    public Object put(String key, Object value) {
        key = key.toLowerCase();
        return super.put(key, value);
    }

    public Object remove(Object key) {
        key = ((String) key).toLowerCase();
        return super.remove(key);
    }

    public boolean containsKey(Object key) {
        key = ((String) key).toLowerCase();
        return super.containsKey(key);
    }

    public void putAll(Map<? extends String, ?> m) {
        int size = m.size();
        if (size > 0) {
            for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

}
