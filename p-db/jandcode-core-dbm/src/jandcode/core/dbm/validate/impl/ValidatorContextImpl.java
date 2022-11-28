package jandcode.core.dbm.validate.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.validate.*;
import jandcode.core.store.*;

import java.util.*;

public class ValidatorContextImpl implements ValidatorContext {

    private Mdb mdb;
    private Object data;
    private IVariantNamed rec;
    private IVariantMap attrs = new VariantMap();

    public ValidatorContextImpl(Mdb mdb, Object data, Map attrs) {
        this.mdb = mdb;
        if (this.mdb == null) {
            throw new XError("mdb=null");
        }
        this.data = data;
        if (attrs != null) {
            this.attrs.putAll(attrs);
        }
        if (this.data instanceof IVariantNamed) {
            this.rec = (IVariantNamed) this.data;
        } else if (this.data instanceof Map) {
            this.rec = new VariantMapWrap((Map) this.data);
        } else {
            throw new XError("data не является IVariantNamed или Map");
        }
    }

    public Mdb getMdb() {
        return mdb;
    }

    public Object getData() {
        return data;
    }

    public IVariantNamed getRec() {
        return rec;
    }

    protected StoreRecord getStoreRec() {
        if (this.rec instanceof StoreRecord) {
            return (StoreRecord) this.rec;
        }
        return null;
    }

    public IVariantMap getAttrs() {
        return attrs;
    }

    ////// validate errors

    public ValidateErrors getValidateErrors() {
        return this.mdb.getValidateErrors();
    }

    public List<ValidateErrorInfo> getErrorInfos() {
        return getValidateErrors().getErrorInfos();
    }

    public void clearErrors() {
        getValidateErrors().clearErrors();
    }

    public boolean hasErrors() {
        return getValidateErrors().hasErrors();
    }

    public boolean hasErrors(int size) {
        return getValidateErrors().hasErrors(size);
    }

    public void checkErrors() {
        getValidateErrors().checkErrors();
    }

    public void addError(ValidateErrorInfo error) {
        getValidateErrors().addError(error);
    }

    public void addError(CharSequence message, String field) {
        getValidateErrors().addError(message, field);
    }

    public void addError(CharSequence message) {
        getValidateErrors().addError(message, getField());
    }

    public void addErrorFatal(CharSequence message) {
        getValidateErrors().addErrorFatal(message, getField());
    }

    //////

    public Model getModel() {
        return this.mdb.getModel();
    }

    public String getField() {
        String s = getAttrs().getString("field");
        if (UtString.empty(s)) {
            return null;
        }
        return s;
    }

    public String getTitle() {
        String s = getAttrs().getString("title");
        if (!UtString.empty(s)) {
            return s;
        }
        StoreRecord sr = getStoreRec();
        if (sr != null) {
            StoreField sf = sr.findField(getField());
            if (sf != null) {
                s = sf.getTitle();
            }
        }
        if (UtString.empty(s)) {
            s = getField();
        }
        if (UtString.empty(s)) {
            return "";
        }
        return s;
    }

    public Domain getDomain() {
        Object dmn = getAttrs().get("domain");
        if (dmn == null) {
            StoreRecord sr = getStoreRec();
            if (sr != null) {
                dmn = sr.getStore().getCustomProp(DbmConsts.STORE_PROP_DOMAIN);
            }
        }
        if (dmn == null) {
            return null;
        }
        if (dmn instanceof CharSequence) {
            dmn = getMdb().getDomain(dmn.toString());
        }
        if (dmn instanceof Domain) {
            return (Domain) dmn;
        }
        if (dmn instanceof IDomainLink) {
            return ((IDomainLink) dmn).getDomain();
        }
        throw new XError("domain {0} не правильный", dmn);
    }

    public Domain getDomain(boolean required) {
        Domain domain = getDomain();
        if (required) {
            throw new XError("Для контекста валидации не определен домен");
        }
        return domain;
    }

    ////// variant

    public Object getValue() {
        return getRec().getValue(getField());
    }

    public Object getValue(String name) {
        return getRec().getValue(name);
    }

    //////

    public boolean validate(String validatorName, Map attrs) throws Exception {
        ValidatorService svc = getModel().bean(ValidatorService.class);
        IVariantMap attrs2 = new VariantMap(getAttrs());
        if (attrs != null) {
            attrs2.putAll(attrs);
        }
        return svc.validatorExec(getMdb(), getData(), validatorName, attrs2);
    }

    public boolean validate(ValidatorDef validatorDef, Map attrs) throws Exception {
        ValidatorService svc = getModel().bean(ValidatorService.class);
        IVariantMap attrs2 = new VariantMap(getAttrs());
        if (attrs != null) {
            attrs2.putAll(attrs);
        }
        return svc.validatorExec(getMdb(), getData(), validatorDef, attrs2);
    }
}
