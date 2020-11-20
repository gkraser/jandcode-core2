package jandcode.core.dbm.domain.impl;

import jandcode.commons.variant.*;
import jandcode.core.dbm.domain.*;

public class IFieldImpl extends BaseFieldMember implements IField {

    private String title;
    private String titleShort;
    private int size;
    private VariantDataType dataType = VariantDataType.OBJECT;
    private String dbDataType;
    private String storeDataType;
    private String ref;
    private String dict;

    //////

    protected void onConfigureMember() throws Exception {
        applyConfProps();
    }

    //////

    public boolean hasRef() {
        return ref != null && ref.length() > 0;
    }

    public boolean hasDict() {
        return dict != null && dict.length() > 0;
    }

    ////// props

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        return titleShort == null ? getTitle() : titleShort;
    }

    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public VariantDataType getDataType() {
        return dataType;
    }

    public void setDataType(VariantDataType dataType) {
        this.dataType = dataType;
    }

    public void setDataTypeName(String dataType) {
        this.dataType = VariantDataType.fromString(dataType);
    }

    public String getDbDataType() {
        return dbDataType == null ? getDataType().toString() : dbDataType;
    }

    public void setDbDataType(String dbDataType) {
        this.dbDataType = dbDataType;
    }

    public String getStoreDataType() {
        return storeDataType == null ? "object" : storeDataType;
    }

    public void setStoreDataType(String storeDataType) {
        this.storeDataType = storeDataType;
    }

    public String getRef() {
        return ref == null ? "" : ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

}
