package jandcode.commons.impl.variant;

import jandcode.commons.variant.*;

/**
 * Обертка для поименнованного значения.
 */
public class ValueNamedVariantNamed extends CustomWrapperVariantNamed {
    public ValueNamedVariantNamed(Object wrapped) {
        super(wrapped);
    }

    public Object getValue(String name) {
        return ((IValueNamed) wrapped).getValue(name);
    }
}
