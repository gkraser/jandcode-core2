package jandcode.core.dbm.validate.std;

import jandcode.core.dbm.validate.*;

import java.text.*;

/**
 * Провека значения поля на min..max
 */
public class Validator_minmax implements Validator {

    public void validate(ValidatorContext ctx) throws Exception {
        double min = ctx.getAttrs().getDouble("min", Double.MIN_VALUE);
        double max = ctx.getAttrs().getDouble("max", Double.MAX_VALUE);
        double value = ctx.getDouble();
        if (value < min || value > max) {
            ctx.addError(
                    MessageFormat.format("Значение поля [{0}] должно быть в диапазоне {1}..{2}",
                            ctx.getTitle(), min, max)
            );
        }
    }
}
