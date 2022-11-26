package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.db.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.store.*;

public class IFieldImpl extends BaseFieldMember implements IField {

    private String title;
    private String titleShort;
    private int size;
    private VariantDataType dataType = VariantDataType.OBJECT;
    private DbDataType dbDataType;
    private StoreDataType storeDataType;
    private String ref;
    private boolean refCascade;
    private String dict;
    private int scale = StoreField.NO_SCALE;
    private boolean req;
    private boolean notNull;

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
        if (UtString.empty(title) && hasRef()) {
            Domain d = getModel().bean(DomainService.class).findDomain(getRef());
            if (d != null) {
                return d.getTitle();
            }
        }
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

    public DbDataType getDbDataType() {
        return dbDataType;
    }

    public void setDbDataType(DbDataType dbDataType) {
        this.dbDataType = dbDataType;
    }

    public void setDbDataType(String dbDataType) {
        this.dbDataType = getModel().getDbSource().getDbDriver().getDbDataTypes().get(dbDataType);
    }

    public StoreDataType getStoreDataType() {
        return storeDataType;
    }

    public void setStoreDataType(StoreDataType storeDataType) {
        this.storeDataType = storeDataType;
    }

    public void setStoreDataType(String storeDataType) {
        this.storeDataType = getApp().bean(StoreService.class).getStoreDataTypes().get(storeDataType);
    }

    public String getSqlType() {
        return getDbDataType().getSqlType(getSize());
    }

    public String getSqlValue(Object value) {
        return getDbDataType().getSqlValue(value);
    }

    public String getRef() {
        return ref == null ? "" : ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isRefCascade() {
        return refCascade;
    }

    public void setRefCascade(boolean refCascade) {
        this.refCascade = refCascade;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setScale(String scale) {
        if (UtString.empty(scale)) {
            this.scale = StoreField.NO_SCALE;
        } else {
            this.scale = UtCnv.toInt(scale, StoreField.NO_SCALE);
        }
    }

    public boolean isReq() {
        return req;
    }

    public void setReq(boolean req) {
        this.req = req;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

}
