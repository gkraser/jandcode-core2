package jandcode.core.auth.impl;

import jandcode.commons.variant.*;
import jandcode.core.auth.*;

import java.util.*;

/**
 * Предок с общей реализацией
 */
public abstract class CustomAuthUser implements AuthUser {

    private Map<String, Object> attrsHolder = new LinkedHashMap<>();
    private IVariantMap attrs = new VariantMapWrap(Collections.unmodifiableMap(attrsHolder));

    public IVariantMap getAttrs() {
        return attrs;
    }

    /**
     * Хранилище атрибутов. Можно писать.
     */
    protected Map<String, Object> getAttrsHolder() {
        return attrsHolder;
    }

}
