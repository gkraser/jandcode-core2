package jandcode.commons.reflect.impl;

import jandcode.commons.*;
import jandcode.commons.reflect.*;

import java.util.*;

public class ReflectTableImpl extends BaseReflectProps implements ReflectTable {

    private Class cls;
    private List<ReflectTableField> fields;
    private Map<String, ReflectTableField> fieldsByName = new HashMap<>();

    public ReflectTableImpl(Class cls, Collection<ReflectTableFieldImpl> fields) {
        this.cls = cls;
        List<ReflectTableField> flds = new ArrayList<>(fields);
        this.fields = Collections.unmodifiableList(flds);
        for (ReflectTableField f : fields) {
            this.fieldsByName.put(UtStrDedup.lower(f.getName()), f);
        }
    }

    public Class getCls() {
        return cls;
    }

    public Collection<ReflectTableField> getFields() {
        return fields;
    }

    public ReflectTableField findField(String name) {
        if (UtString.empty(name)) {
            return null;
        }
        return fieldsByName.get(name.toLowerCase());
    }


}
