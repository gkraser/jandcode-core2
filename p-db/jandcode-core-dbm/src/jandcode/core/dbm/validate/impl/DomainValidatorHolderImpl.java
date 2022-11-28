package jandcode.core.dbm.validate.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.validate.*;

import java.util.*;

public class DomainValidatorHolderImpl extends BaseDomainMember implements DomainValidatorHolder {

    private NamedList<ValidatorDef> validators = new DefaultNamedList<>("Валидатор {0} не зарегистрирован");

    protected void onConfigureMember() throws Exception {

        // собственные валидаторы
        grabValidators(this.validators, getDomain().getConf(), null);

        // собираем валидаторы со всех полей
        for (Field field : getDomain().getFields()) {
            Conf conf = field.getConf();
            grabValidators(this.validators, conf, field);
        }

    }

    private void grabValidators(NamedList<ValidatorDef> dest, Conf conf, Field field) {
        ValidatorService svc = getModel().bean(ValidatorService.class);
        String fieldName = field == null ? "" : field.getName();
        String nmPfx = field == null ? "" : field.getName() + ":";

        // собираем из атрибутов validator.xxx
        for (String key : conf.keySet()) {
            String valName = UtString.removePrefix(key, "validator.");
            if (valName == null) {
                continue;
            }
            boolean flag = UtCnv.toBoolean(conf.get(key));
            if (!flag) {
                continue;
            }
            Conf tmp = Conf.create(nmPfx + valName);
            tmp.setValue("type", valName);
            tmp.setValue("field", fieldName);
            ValidatorDef vd2 = svc.createValidatorDef(tmp);
            dest.add(vd2);
        }

        // собираем из дочерних тегов validator
        for (Conf x : conf.getConfs("validator")) {
            String nm = nmPfx + x.getName();
            Conf x1 = Conf.create(nm);
            x1.join(x);
            x1.setValue("field", fieldName);
            if (UtString.empty(x1.getString("class")) && UtString.empty(x1.getString("type"))) {
                x1.setValue("type", x.getName());
            }
            ValidatorDef vd2 = svc.createValidatorDef(x1);
            dest.add(vd2);
        }

    }

    public NamedList<ValidatorDef> getValidators() {
        return validators;
    }

    public List<ValidatorDef> getValidatorsField(String fieldName) {
        List<ValidatorDef> res = new ArrayList<>();
        for (ValidatorDef vd : getValidators()) {
            String f1 = vd.getConf().getString("field");
            if (UtString.empty(f1)) {
                continue;
            }
            if (f1.equalsIgnoreCase(fieldName)) {
                res.add(vd);
            }
        }
        return res;
    }

    public List<ValidatorDef> getValidatorsRecord() {
        List<ValidatorDef> res = new ArrayList<>();
        for (ValidatorDef vd : getValidators()) {
            String f1 = vd.getConf().getString("field");
            if (!UtString.empty(f1)) {
                continue;
            }
            res.add(vd);
        }
        return res;
    }

}
