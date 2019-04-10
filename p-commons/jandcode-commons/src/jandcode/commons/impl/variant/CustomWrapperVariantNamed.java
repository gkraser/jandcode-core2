package jandcode.commons.impl.variant;

import jandcode.commons.variant.*;

/**
 * Предок для простого создания оберток для IVariantNamed.
 */
public abstract class CustomWrapperVariantNamed implements IVariantNamed {

    protected Object wrapped;

    public CustomWrapperVariantNamed(Object wrapped) {
        this.wrapped = wrapped;
    }

}
