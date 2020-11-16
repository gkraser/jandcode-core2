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
    private boolean calc;
    private String ref;
    private boolean req;

    //////

    protected void onConfigureMember() throws Exception {
        applyRtAttrs();
    }

    //////

    public boolean hasRef() {
        return ref != null && ref.length() > 0;
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

    public void setDbDDataType(String dbDDataType) {
        this.dbDataType = dbDDataType;
    }

    public String getStoreDataType() {
        return storeDataType == null ? "object" : storeDataType;
    }

    public void setStoreDataType(String storeDataType) {
        this.storeDataType = storeDataType;
    }

    public boolean isCalc() {
        return calc;
    }

    public void setCalc(boolean calc) {
        this.calc = calc;
    }

    public String getRef() {
        return ref == null ? "" : ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isReq() {
        return req;
    }

    public void setReq(boolean req) {
        this.req = req;
    }

}
