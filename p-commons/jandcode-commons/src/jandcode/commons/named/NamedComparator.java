package jandcode.commons.named;

import java.util.*;

/**
 * Сревнение объектов INamed.
 * По умолчанию - без учета регистра.
 */
public class NamedComparator implements Comparator<INamed> {

    private boolean _ignoreCase = true;

    public NamedComparator() {
    }

    public NamedComparator(boolean ignoreCase) {
        _ignoreCase = ignoreCase;
    }

    public int compare(INamed o1, INamed o2) {
        if (_ignoreCase) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        } else {
            return o1.getName().compareTo(o2.getName());
        }
    }
}