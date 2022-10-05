package jandcode.core.dbm.sql.impl;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

public class MapFilterValueImpl implements MapFilterValue {

    private String key;
    private IVariantMap props = new VariantMap();

    /**
     * @param key   ключ
     * @param value данные. Может быть map или значением, которое будет
     *              преобразовано в props[value:value]
     */
    public MapFilterValueImpl(String key, Object value) {
        this.init(key, value);
    }

    //////

    private void init(String key, Object value) {
        this.key = key;
        if (UtString.empty(this.key)) {
            this.key = "key";
        }
        if (value instanceof Map) {
            this.props.putAll((Map) value);
            if (!this.props.containsKey("value")) {
                this.props.put("value", null);
            }
        } else {
            this.props.put("value", value);
        }
    }

    //////

    public IVariantMap getProps() {
        return props;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return getProps().get("value");
    }

    public List<String> getValueList() {
        return UtCnv.toList(getValue());
    }

    public Set<Long> getValueIds() {
        Set<Long> ids = new LinkedHashSet<>();
        List<String> strs = getValueList();
        for (String s : strs) {
            long v = UtCnv.toLong(s);
            if (v != 0) {
                ids.add(v);
            }
        }
        return ids;
    }

    public String paramName(String suffix) {
        if (UtString.empty(suffix)) {
            suffix = "value";
        }
        return getKey() + "__" + suffix;
    }

}
