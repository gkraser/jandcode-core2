package jandcode.commons.simxml.impl;

import jandcode.commons.*;
import jandcode.commons.variant.*;

import java.util.*;

/**
 * Атрибуты SimXml
 */
public class SimXmlAttrs extends VariantMap {

    public Object put(String key, Object value) {
        key = UtStrDedup.normal(key);
        return super.put(key, value);
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
