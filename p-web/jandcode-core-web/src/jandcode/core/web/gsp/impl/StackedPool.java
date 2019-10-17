package jandcode.core.web.gsp.impl;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Абстрактный стековый пул поименованных компонентов
 */
public abstract class StackedPool<TYPE> {

    private HashMap<String, Item> items = new HashMap<String, Item>();

    /**
     * Элемент пула
     */
    class Item extends Named {

        /**
         * Экземпляры
         */
        ArrayList<TYPE> insts = new ArrayList<TYPE>();

        /**
         * Позиция в списке первого свободного
         */
        int pos;

        /**
         * Взять объект из пула
         */
        TYPE getObject() throws Exception {
            TYPE c;
            if (insts.size() <= pos) {
                // пул пустой
                c = createInstance(getName());
                insts.add(c);
            } else {
                c = insts.get(pos);
                if (c == null) {
                    // если в этом месте null, кто-то забрал объект навсегда, еще раз создаем
                    c = createInstance(getName());
                    insts.set(pos, c);
                }
            }
            pos++;
            return c;
        }

        /**
         * Вернуть объект в пул.
         *
         * @param remove true - значит объект нужно удалить из пула, он как независимый
         *               экземпляр понадобился
         */
        void returnObject(boolean remove) {
            pos--;
            if (pos < 0) {
                throw new XErrorBadPool();
            }
            if (remove) {
                insts.set(pos, null);
            }
        }

    }

    /**
     * Создать новый экземпляр объекта с указанным именем
     */
    protected abstract TYPE createInstance(String name) throws Exception;

    //////

    /**
     * Получить объект из пула
     *
     * @param name имя объекта
     * @return экземпляр объекта
     */
    public TYPE getObject(String name) throws Exception {
        Item a = items.get(name);
        if (a == null) {
            a = new Item();
            a.setName(name);
            items.put(name, a);
        }
        //
        return a.getObject();
    }

    /**
     * Вернуть последний взятый объект в пул
     *
     * @param name   имя объекта
     * @param remove true - значит объект нужно удалить из пула, он как независимый
     *               экземпляр понадобился
     */
    public void returnObject(String name, boolean remove) {
        Item a = items.get(name);
        if (a == null) {
            throw new XErrorBadPool();
        }
        a.returnObject(remove);
    }

}
