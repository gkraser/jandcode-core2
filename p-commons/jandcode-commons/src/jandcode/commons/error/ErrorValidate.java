package jandcode.commons.error;

import java.util.*;

/**
 * Интерфейс для ошибок валидации (для Exception)
 */
public interface ErrorValidate {

    String FIELD = "field"; //NON-NLS
    String TEXT = "text"; //NON-NLS

    /**
     * Список ошибок валидации.
     * Каждый Map в списке должен иметь ключи: field и text
     */
    List<Map<String, String>> getValidateErrors();

}
