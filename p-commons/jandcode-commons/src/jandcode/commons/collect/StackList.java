package jandcode.commons.collect;

import java.util.*;

/**
 * Список - стек
 */
public class StackList<TYPE> extends ArrayList<TYPE> {

    public StackList() {
    }

    /**
     * Удалить последний элемент.
     *
     * @return возвращает удаленный элемент или null, если список пустой
     */
    public TYPE pop() {
        if (size() > 0) {
            return remove(size() - 1);
        }
        return null;
    }

    /**
     * Элемент в стек. Возвращает добавленный
     */
    public TYPE push(TYPE it) {
        add(it);
        return it;
    }

    /**
     * Последний элемент списка или null, если нет элементов в списке
     */
    public TYPE last() {
        if (size() == 0) {
            return null;
        } else {
            return get(size() - 1);
        }
    }

    /**
     * Первый элемент списка или null, если нет элементов в списке
     */
    public TYPE first() {
        if (size() == 0) {
            return null;
        } else {
            return get(0);
        }
    }

}