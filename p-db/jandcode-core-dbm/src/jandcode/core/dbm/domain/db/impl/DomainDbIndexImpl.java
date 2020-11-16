package jandcode.core.dbm.domain.db.impl;

import jandcode.core.*;
import jandcode.core.dbm.domain.db.*;

import java.util.*;

public class DomainDbIndexImpl extends BaseComp implements DomainDbIndex {

    private List<DomainDbIndexField> fields = new ArrayList<>();
    private boolean unique;

    /**
     * Разделяем на имена через ','. Если поле начинается с '*', то desc=true
     */
    public void setFields(String fields) {
        this.fields.clear();
        String[] a = fields.split(",");
        for (String b : a) {
            boolean desc = false;
            String n = b;
            if (n.startsWith("*")) {
                n = n.substring(1);
                desc = true;
            }
            DomainDbIndexField f = new DomainDbIndexFieldImpl(n, desc);
            this.fields.add(f);
        }
    }

    public List<DomainDbIndexField> getFields() {
        return fields;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getSqlFields() {
        StringBuilder sb = new StringBuilder();
        for (DomainDbIndexField fi : getFields()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(fi.getName());
            if (fi.isDesc()) {
                sb.append(" desc");
            }
        }
        return sb.toString();
    }

}
