package jandcode.core.dbm.validate.std;

import jandcode.core.dbm.validate.*;

import java.text.*;

/**
 * Проверка notnull значения
 */
public class Validator_notnull implements Validator {

    public void validate(ValidatorContext ctx) throws Exception {
        if (ctx.getRec().isValueNull(ctx.getField())) {
            ctx.addError(
                    MessageFormat.format("Значение для поля [{0}] не может быть пустым", ctx.getTitle())
            );
        }
    }

}
