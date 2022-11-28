package jandcode.commons.error.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;

public class ValidateErrorInfoImpl implements ValidateErrorInfo {

    private CharSequence message;
    private String field;

    public ValidateErrorInfoImpl(CharSequence message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        String s = null;
        if (message != null) {
            s = message.toString();
        }
        if (UtString.empty(s)) {
            s = field;
        }
        if (UtString.empty(s)) {
            s = "Error";
        }
        return s;
    }

    public String getField() {
        return field;
    }

    public String toString() {
        return getMessage();
    }

}
