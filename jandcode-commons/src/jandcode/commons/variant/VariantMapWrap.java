package jandcode.commons.variant;

import java.util.*;

/**
 * Враппер для Map с доступом IVariantNamed.
 * Ключ - строка, значение - объект.
 */
@SuppressWarnings("unchecked")
public class VariantMapWrap implements IVariantMap {

    protected Map _map;

    public VariantMapWrap() {
    }

    public VariantMapWrap(Map map) {
        _map = map;
    }

    public Map getMap() {
        return _map;
    }

    public void setMap(Map map) {
        _map = map;
    }

    //////

    public Object getValue(String name) {
        return get(name);
    }

    public void setValue(String name, Object value) {
        put(name, value);
    }

    public Object get(String name, Object defValue) {
        Object v = get(name);
        if (v == null) {
            return defValue;
        }
        return v;
    }

    ////// map

    public int size() {
        return getMap().size();
    }

    public boolean isEmpty() {
        return getMap().isEmpty();
    }

    public boolean containsKey(Object key) {
        return getMap().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return getMap().containsValue(value);
    }

    public Object get(Object key) {
        return getMap().get(key);
    }

    public Object put(String key, Object value) {
        return getMap().put(key, value);
    }

    public Object remove(Object key) {
        return getMap().remove(key);
    }

    public void putAll(Map m) {
        getMap().putAll(m);
    }

    public void clear() {
        getMap().clear();
    }

    public Set keySet() {
        return getMap().keySet();
    }

    public Collection values() {
        return getMap().values();
    }

    public Set entrySet() {
        return getMap().entrySet();
    }

    //////

    public String toString() {
        return getMap().toString();
    }

}