package jandcode.core.dbm.validate.std;

import jandcode.commons.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.validate.*;

import java.util.*;

/**
 * Проверка поля
 */
public class Validator_field implements Validator {

    public void validate(ValidatorContext ctx) throws Exception {
        Domain domain = ctx.getDomain(true);
        Field field = domain.getField(ctx.getField());
        //
        Object value = ctx.getRec().getValue(field.getName());
        boolean empty = UtCnv.isEmpty(value);
        boolean req = field.isReq();
        boolean notnull = field.isNotNull();
        //
        if (req) {
            if (!ctx.validate("req")) {
                return;
            }
        }
        if (notnull) {
            if (!ctx.validate("notnull")) {
                return;
            }
        }
        if (!req && empty) {
            return; // дальше бессмысленно - поле пустое, проверки не пройдут все равно
        }
        //
        List<ValidatorDef> vds = domain.bean(DomainValidatorHolder.class).getValidatorsField(field.getName());
        for (ValidatorDef vd : vds) {
            if (!ctx.validate(vd)) {
                // на первом не сработавшем валидаторе для поля - выходим, остальные врядли правильные
                return;
            }
        }
    }

}
