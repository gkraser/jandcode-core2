package jandcode.commons.collect;

import jandcode.commons.*;

import java.util.*;

/**
 * Объект для хранения некоторых объектов с привязкой к классам.
 * Добавляем объект для класса, затем пожем получить этот объект
 * как для добавленного класса, так и для его наследников.
 */
@SuppressWarnings("unchecked")
public class ClassLinks<TYPE> {

    private TYPE nullData;
    private List<Item> items = new ArrayList<Item>();
    private Map<Class, TYPE> dataByClass = new HashMap<Class, TYPE>();
    private boolean sorted;

    class Item {
        Class cls;
        TYPE data;
    }

    /**
     * Добавить данные для класса
     *
     * @param cls  для какого класса
     * @param data что за данные
     */
    public void add(Class cls, TYPE data) {
        if (cls == null) {
            nullData = data;
            return;
        }
        sorted = false;
        Item it = new Item();
        it.cls = cls;
        it.data = data;
        items.add(it);
        dataByClass.put(cls, data);
    }

    /**
     * Добавить данные для класса
     *
     * @param className для какого класса
     * @param data      что за данные
     */
    public void add(String className, TYPE data) {
        if (className == null || className.equals("null")) { //NON-NLS
            add((Class) null, data);
        } else {
            add(UtClass.getClass(className), data);
        }
    }

    /**
     * Получить данные для класса
     *
     * @param cls для какого класса
     * @return null, если нет привязки
     */
    public TYPE get(Class cls) {
        if (cls == null) {
            return nullData;
        }
        TYPE r = dataByClass.get(cls);
        if (r == null) {
            for (Item z : getItems()) {
                if (z.cls.isAssignableFrom(cls)) {
                    r = z.data;
                    break;
                }
            }
            synchronized (this) {
                dataByClass.put(cls, r);
            }
        }
        return r;
    }

    /**
     * Получить данные для класса
     *
     * @param className для какого класса
     * @return null, если нет привязки
     */
    public TYPE get(String className) {
        if (className == null || className.equals("null")) { //NON-NLS
            return get((Class) null);
        }
        return get(UtClass.getClass(className));
    }

    //////

    private List<Item> getItems() {
        if (!sorted) {
            synchronized (this) {
                if (!sorted) {
                    // сортируем по наследственности. Более общие предки должны быть дальше от конца
                    Collections.sort(items, new Comparator<Item>() {
                        public int compare(Item o1, Item o2) {
                            if (o2.cls.isAssignableFrom(o1.cls)) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });
                }
                sorted = true;
            }
        }
        return items;
    }

    //////

    public void clear() {
        items.clear();
        dataByClass.clear();
        nullData = null;
    }
}
