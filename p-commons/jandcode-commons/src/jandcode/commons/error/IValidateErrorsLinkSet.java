package jandcode.commons.error;

/**
 * Интерфейс для классов, которые могут получать ссылку на ошибки валидации
 */
public interface IValidateErrorsLinkSet {

    /**
     * Ошибки валидации
     */
    void setValidateErrors(ValidateErrors validateErrors);

}
