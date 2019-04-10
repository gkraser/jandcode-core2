package jandcode.commons.named;

import jandcode.commons.impl.*;

/**
 * Список поименнованных объектов {@link NamedList} по умолчанию
 */
public class DefaultNamedList<TYPE extends INamed> extends NamedListImpl<TYPE> implements NamedList<TYPE> {

    public DefaultNamedList() {
    }

    /**
     * см: {@link NamedList#setNotFoundMessage(java.lang.Object)}
     */
    public DefaultNamedList(Object notFoundMessage) {
        setNotFoundMessage(notFoundMessage);
    }

    /**
     * см: {@link NamedList#setNotFoundMessage(java.lang.Object, java.lang.Object...)}
     */
    public DefaultNamedList(Object notFoundMessage, Object... params) {
        setNotFoundMessage(notFoundMessage, params);
    }

}
