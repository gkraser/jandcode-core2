package jandcode.commons.variant;

import java.util.*;

/**
 * Типизированная map
 */
public interface IVariantMap extends Map<String, Object>, IValueNamedSet, IVariantNamed, IVariantNamedDefault {

    /**
     * Возвращается defValue, если get(key)==null
     */
    Object get(String key, Object defValue);

}
