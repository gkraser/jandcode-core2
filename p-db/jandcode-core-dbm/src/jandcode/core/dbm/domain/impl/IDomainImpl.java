package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

public class IDomainImpl extends BaseDomainMember implements IDomain {

    private String title;
    private String dbTableName;
    private Map<String, String> tags = new HashMap<>();

    protected void onConfigureMember() throws Exception {
        applyConfProps();

        Conf x = getDomain().getConf();

        // tags
        for (Map.Entry<String, Object> a : x.entrySet()) {
            String tagName = UtString.removePrefix(a.getKey(), "tag.");
            if (tagName != null) {
                String v = UtCnv.toString(a.getValue());
                if (!UtString.empty(v)) {
                    tags.put(tagName, v);
                }
            }
        }

    }

    //////


    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDbTableName() {
        return dbTableName == null ? "" : dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        return tags.containsKey(tag);
    }

}
