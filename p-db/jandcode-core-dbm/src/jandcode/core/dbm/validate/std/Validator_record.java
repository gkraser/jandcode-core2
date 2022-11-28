package jandcode.core.dbm.validate.std;

import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.validate.*;

import java.util.*;

/**
 * Проверка записи целиком.
 */
public class Validator_record implements Validator {

    public void validate(ValidatorContext ctx) throws Exception {
        Domain domain = ctx.getDomain(true);
        int ers = ctx.getErrorInfos().size(); // запоминаем для дальнейшего уточнения - возникли ли ошибки
        //
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("domain", domain);
        for (Field field : domain.getFields()) {
            attrs.put("field", field.getName());
            ctx.validate("field", attrs);
        }
        //
        if (ctx.hasErrors(ers)) {
            // в полях были ошибки, запись целиком проверять бессмысленно
            return;
        }
        //
        List<ValidatorDef> vds = domain.bean(DomainValidatorHolder.class).getValidatorsRecord();
        for (ValidatorDef vd : vds) {
            if (!ctx.validate(vd)) {
                // на первом не сработавшем валидаторе - выходим, остальные врядли правильные
                return;
            }
        }
    }

}
