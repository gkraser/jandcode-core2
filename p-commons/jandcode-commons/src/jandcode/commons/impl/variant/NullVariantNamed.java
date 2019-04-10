package jandcode.commons.impl.variant;

/**
 * Для любого имени возвращается null.
 */
public class NullVariantNamed extends CustomWrapperVariantNamed {

    public NullVariantNamed() {
        super(null);
    }

    public Object getValue(String name) {
        return null;
    }

}
