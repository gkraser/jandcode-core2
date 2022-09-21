package jandcode.core.dbm.validate.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.validate.*;

public class ValidatorServiceImpl extends BaseModelMember implements ValidatorService {

    private NamedList<ValidatorDef> validators = new DefaultNamedList<>("Валидатор {0} не зарегистрирован");

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf conf = getModel().getConf();
        for (Conf x : conf.getConfs("validator")) {
            if (UtString.empty(x.getString("class"))) {
                throw new XError("Не указан атрибут class: {0}", conf.origin());
            }
            ValidatorDef vd = createValidatorDef(x);
            this.validators.add(vd);
        }
    }

    public NamedList<ValidatorDef> getValidators() {
        return validators;
    }

    public ValidatorDef createValidatorDef(Conf conf) {
        Conf conf2 = conf;
        Object cn = conf.getValue("class");
        if (cn == null) {
            String type = conf.getString("type");
            if (UtString.empty(type)) {
                type = conf.getName();
            }
            ValidatorDef proto = getValidators().get(type);
            conf2 = Conf.create(conf.getName());
            conf2.join(proto.getConf());
            conf2.join(conf);
        }
        return new ValidatorDefImpl(conf2.getName(), conf2, getModel());
    }

}
