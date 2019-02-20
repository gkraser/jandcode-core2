package jandcode.commons.impl.variant;

/**
 * Имя = id -> возвращается значение, в остальных случаях - null.
 */
public class IdVariantNamed extends CustomWrapperVariantNamed {

    public IdVariantNamed(Object wrapped) {
        super(wrapped);
    }

    public Object getValue(String name) {
        if ("id".equalsIgnoreCase(name)) {
            return wrapped;
        }
        return null;
    }
}
