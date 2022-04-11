package jandcode.core.dbm.sql.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.sql.*;
import jandcode.core.store.*;

import java.util.*;

public class SqlBuilderImpl extends BaseModelMember implements IModelMember, SqlBuilder {

    /**
     * true - если поле является полем данных.
     * Исключаются из этого понятия поля, содержащие '-'.
     */
    protected boolean isDataFieldName(String name) {
        return name != null && name.indexOf('-') == -1;
    }

    public List<String> makeFieldList(Object fields) {
        Map<String, String> res = new LinkedHashMap<>();
        if (fields != null) {
            if (fields instanceof Domain || fields instanceof IDomainLink) {
                Domain t;
                if (fields instanceof Domain) {
                    t = (Domain) fields;
                } else {
                    t = ((IDomainLink) fields).getDomain();
                }
                for (Field f : t.getFields()) {
                    if (isDataFieldName(f.getName())) {
                        res.putIfAbsent(f.getName().toLowerCase(), f.getName());
                    }
                }

            } else if (fields instanceof IStoreFieldHolder) {
                IStoreFieldHolder t = (IStoreFieldHolder) fields;
                for (StoreField f : t.getFields()) {
                    if (!isDataFieldName(f.getName())) {
                        continue;
                    }
                    res.putIfAbsent(f.getName().toLowerCase(), f.getName());
                }

            } else if (fields instanceof Map) {
                Map t = (Map) fields;
                for (Object f : t.keySet()) {
                    String fn = UtString.toString(f);
                    if (!isDataFieldName(fn)) {
                        continue;
                    }
                    res.putIfAbsent(fn.toLowerCase(), fn);
                }

            } else {
                List<String> t = UtCnv.toList(fields);
                for (String fn : t) {
                    if (!isDataFieldName(fn)) {
                        continue;
                    }
                    res.putIfAbsent(fn.toLowerCase(), fn);
                }
            }
        }
        return new ArrayList<>(res.values());
    }

    public List<String> makeFieldList(Object fields, Object excludeFields) {
        List<String> res = makeFieldList(fields);
        if (excludeFields != null) {
            List<String> excludeList = makeFieldList(excludeFields);
            List<String> res2 = new ArrayList<>();
            for (String nm : res) {
                boolean exclude = false;
                for (String exnm : excludeList) {
                    if (exnm.equalsIgnoreCase(nm)) {
                        exclude = true;
                        break;
                    }
                }
                if (!exclude) {
                    res2.add(nm);
                }
            }
            return res2;
        }
        return res;
    }

    public String makeSqlInsert(String tableName, Object fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ")
                .append(tableName)
                .append("(");
        List<String> flds = makeFieldList(fields);
        if (flds.size() == 0) {
            throw new XError("Не указаны поля в fields для makeSqlInsert");
        }
        int cnt = 0;
        for (String f : flds) {
            if (cnt != 0) {
                sb.append(",");
            }
            sb.append(f);
            cnt++;
        }
        sb.append(") values (");
        cnt = 0;
        for (String f : flds) {
            if (cnt != 0) {
                sb.append(",");
            }
            sb.append(":");
            sb.append(f);
            cnt++;
        }
        sb.append(")");
        return sb.toString();
    }

    public String makeSqlUpdate(String tableName, Object fields, Object whereFields) {
        StringBuilder sb = new StringBuilder();
        sb.append("update ")
                .append(tableName)
                .append(" set ");
        List<String> flds = makeFieldList(fields);
        if (flds.size() == 0) {
            throw new XError("Не указаны поля в fields для makeSqlUpdate");
        }
        int cnt = 0;
        for (String f : flds) {
            if (cnt != 0) {
                sb.append(",");
            }
            sb.append(f);
            sb.append("=:");
            sb.append(f);
            cnt++;
        }
        sb.append(" where ");
        //
        List<String> whereFlds = makeFieldList(whereFields);
        if (whereFlds.size() == 0) {
            throw new XError("Не указаны поля в whereFields для makeSqlUpdate");
        }
        cnt = 0;
        for (String f : whereFlds) {
            if (cnt != 0) {
                sb.append(" and ");
            }
            sb.append(f);
            sb.append("=:");
            sb.append(f);
            cnt++;
        }
        return sb.toString();
    }

    public String makeSqlDelete(String tableName, Object whereFields) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ")
                .append(tableName)
                .append(" where ");
        //
        List<String> whereFlds = makeFieldList(whereFields);
        if (whereFlds.size() == 0) {
            throw new XError("Не указаны поля в whereFields для makeSqlDelete");
        }
        int cnt = 0;
        for (String f : whereFlds) {
            if (cnt != 0) {
                sb.append(" and ");
            }
            sb.append(f);
            sb.append("=:");
            sb.append(f);
            cnt++;
        }
        return sb.toString();
    }

}
