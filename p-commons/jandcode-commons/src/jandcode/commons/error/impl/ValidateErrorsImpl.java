package jandcode.commons.error.impl;

import jandcode.commons.error.*;

import java.util.*;

public class ValidateErrorsImpl implements ValidateErrors {

    private List<ValidateErrorInfo> errors = new ArrayList<>();


    public List<ValidateErrorInfo> getErrorInfos() {
        return errors;
    }

    public void clearErrors() {
        errors.clear();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public boolean hasErrors(int size) {
        return errors.size() > size;
    }

    public void checkErrors() {
        if (hasErrors()) {
            // делаем копию, т.к. ошибки могут быть очищенны в любой момент
            ValidateErrorsImpl tmp = new ValidateErrorsImpl();
            tmp.errors.addAll(this.errors);
            throw new XErrorValidate(tmp);
        }
    }

    public void addError(ValidateErrorInfo error) {
        if (error == null) {
            return;
        }
        this.errors.add(error);
    }

    public void addError(CharSequence message, String field, Object data) {
        this.errors.add(new ValidateErrorInfoImpl(message, field, data));
    }

    //////

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ValidateErrorInfo error : errors) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(error.toString());
        }
        return sb.toString().trim();
    }

}
