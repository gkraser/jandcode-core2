package jandcode.core.dbm.validate;

/**
 * Валидатор
 */
public interface Validator {

    /**
     * Выполнить валидацию
     *
     * @param ctx контекст валидации
     */
    void validate(ValidatorContext ctx) throws Exception;

}
