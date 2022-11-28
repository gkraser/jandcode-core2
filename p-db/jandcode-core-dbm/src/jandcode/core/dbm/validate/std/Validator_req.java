package jandcode.core.dbm.validate.std;

import jandcode.commons.*;
import jandcode.core.dbm.validate.*;

import java.text.*;

/**
 * Проверка обязательности значения
 */
public class Validator_req implements Validator {

    public void validate(ValidatorContext ctx) throws Exception {
        Object v = ctx.getValue();
        if (UtCnv.isEmpty(v)) {
            ctx.addError(
                    MessageFormat.format("Значение для поля [{0}] обязательно", ctx.getTitle())
            );
        }
    }

}
