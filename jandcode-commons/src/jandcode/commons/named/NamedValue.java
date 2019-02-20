package jandcode.commons.named;

import jandcode.commons.variant.*;

/**
 * Объект с именем и значением.
 * Имя объекта по умолчанию регистронезависимое.
 */
public class NamedValue extends Named implements INamedValue, IValueSet {

    private Object value;

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public String toString() {
        return getName() + "=" + getValue();
    }
}
