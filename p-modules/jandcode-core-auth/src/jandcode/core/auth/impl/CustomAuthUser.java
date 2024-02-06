package jandcode.core.auth.impl;

import jandcode.commons.variant.*;
import jandcode.core.auth.*;

/**
 * Предок с общей реализацией
 */
public abstract class CustomAuthUser implements AuthUser {

    private IVariantMap attrs = new VariantMap();

    public IVariantMap getAttrs() {
        return attrs;
    }

}
