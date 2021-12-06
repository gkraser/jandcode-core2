package jandcode.commons.reflect.impl;

import jandcode.commons.*;
import jandcode.commons.reflect.*;

import java.util.*;

public class ReflectRecordImpl extends BaseReflectProps implements ReflectRecord {

    private Class cls;
    private List<ReflectRecordField> fields;
    private Map<String, ReflectRecordField> fieldsByName = new HashMap<>();

    public ReflectRecordImpl(Class cls, Collection<ReflectRecordFieldImpl> fields) {
        this.cls = cls;
        List<ReflectRecordField> flds = new ArrayList<>(fields);
        this.fields = Collections.unmodifiableList(flds);
        for (ReflectRecordField f : fields) {
            this.fieldsByName.put(UtStrDedup.lower(f.getName()), f);
        }
    }

    public Class getCls() {
        return cls;
    }

    public Collection<ReflectRecordField> getFields() {
        return fields;
    }

    public ReflectRecordField findField(String name) {
        if (UtString.empty(name)) {
            return null;
        }
        return fieldsByName.get(name.toLowerCase());
    }


}
