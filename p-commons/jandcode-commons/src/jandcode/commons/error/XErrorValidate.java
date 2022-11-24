package jandcode.commons.error;

import java.util.*;

/**
 * Ошибка валидации
 */
public class XErrorValidate extends RuntimeException implements ErrorValidate {

    private ValidateErrors errors;

    public XErrorValidate(ValidateErrors errors) {
        this.errors = errors;
    }

    public ValidateErrors getErrors() {
        return errors;
    }

    public String getMessage() {
        return getErrors().toString();
    }

    public List<Map<String, String>> getValidateErrors() {
        List<Map<String, String>> res = new ArrayList<>();
        for (ValidateErrorInfo info : getErrors().getErrorInfos()) {
            Map<String, String> m = new LinkedHashMap<>();
            m.put(ErrorValidate.FIELD, info.getField());
            m.put(ErrorValidate.MESSAGE, info.getMessage());
            res.add(m);
        }
        return res;
    }
    
}
