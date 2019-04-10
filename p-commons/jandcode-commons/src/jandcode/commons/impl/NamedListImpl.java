package jandcode.commons.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;

import java.text.*;
import java.util.*;

public abstract class NamedListImpl<TYPE extends INamed> implements NamedList<TYPE> {

    protected Map<String, TYPE> map = new HashMap<>();
    protected List<TYPE> list = new ArrayList<>();
    private boolean ignoreCase = true;
    private Object notFoundMessage;

    class DefaultNamedNotFoundMessage implements NamedNotFoundMessage {

        String msg;
        Object[] params;

        DefaultNamedNotFoundMessage(String msg, Object[] params) {
            this.msg = msg;
            this.params = params;
        }

        public String makeNotFoundMessage(String name) {
            Object[] p;
            if (params == null) {
                p = new Object[1];
            } else {
                p = new Object[params.length + 1];
            }
            p[0] = name;
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    Object z = params[i];
                    if (z == null) {
                        z = "";
                    } else if (z instanceof INamed) {
                        z = ((INamed) z).getName();
                    } else {
                        z = z.toString();
                    }
                    p[i + 1] = z;
                }
            }
            return MessageFormat.format(msg, p);
        }

    }

    public TYPE find(String name) {
        String key = makeKey(name);
        return map.get(key);
    }

    public TYPE get(String name) {
        String key = makeKey(name);
        TYPE it = map.get(key);
        if (it == null) {
            throw new XError(makeNotFoundMessage(name));
        }
        return it;
    }

    public void sort() {
        sort(new NamedComparator());
    }

    public List<String> getNames() {
        List<String> res = new ArrayList<>();
        for (INamed z : this) {
            res.add(z.getName());
        }
        return res;
    }

    public void setNotFoundMessage(Object notFoundMessage) {
        this.notFoundMessage = notFoundMessage;
    }

    public void setNotFoundMessage(Object notFoundMessage, Object... params) {
        if (notFoundMessage == null || !(notFoundMessage instanceof CharSequence)) {
            this.notFoundMessage = notFoundMessage;
        } else {
            this.notFoundMessage = new DefaultNamedNotFoundMessage(notFoundMessage.toString(), params);
        }
    }

    //////

    protected String makeNotFoundMessage(String key) {
        if (notFoundMessage instanceof CharSequence) {
            return MessageFormat.format(notFoundMessage.toString(), key);
        }
        if (notFoundMessage instanceof NamedNotFoundMessage) {
            return ((NamedNotFoundMessage) notFoundMessage).makeNotFoundMessage(key);
        }
        return MessageFormat.format("Не найден объект [{0}]", key);
    }

    /**
     * Вызывается перед добавлением элемента в список
     */
    protected void onAdd(TYPE it) {
        String n = it.getName();
        if (n == null || n.length() == 0) {
            throw new XError("Объекту не установлено имя, он не может быть помещен в список NamedList");
        }
    }

    /**
     * Вызывается после удаления элемента из списка
     */
    protected void onRemove(TYPE it) {
    }

    /**
     * Возвращает ключ, с которым будет хранится в Map
     */
    protected String makeKey(Object key) {
        String s = UtString.toString(key);
        if (ignoreCase) {
            s = s.toLowerCase();
        }
        return s;
    }

    ////// Collection interface

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        if (o instanceof CharSequence) {
            String key = makeKey(o);
            return map.containsKey(key);
        } else {
            return list.contains(o);
        }
    }

    public Iterator<TYPE> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public boolean add(TYPE it) {
        onAdd(it);
        String key = makeKey(it.getName());
        if (map.containsKey(key)) {
            TYPE itPrev = map.remove(key);
            int idx = list.indexOf(itPrev);
            map.put(key, it);
            list.set(idx, it);
            onRemove(itPrev);
        } else {
            map.put(key, it);
            list.add(it);
        }
        return true;
    }

    public boolean remove(Object o) {
        if (o instanceof CharSequence) {
            String key = makeKey(o);
            TYPE a = map.remove(key);
            if (a != null) {
                list.remove(a);
                onRemove(a);
            }
        } else if (o instanceof Number) {
            remove(((Number) o).intValue());
        } else {
            String key = makeKey(((TYPE) o).getName());
            TYPE a = map.remove(key);
            if (a != null) {
                list.remove(a);
                onRemove(a);
            }
        }
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection<? extends TYPE> c) {
        if (c != null) {
            for (TYPE it : c) {
                add(it);
            }
            return c.size() > 0;
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        if (c != null) {
            for (Object it : c) {
                remove(it);
            }
            return c.size() > 0;
        }
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll");
    }

    public void clear() {
        list.clear();
        map.clear();
    }

    //////  List interface

    public boolean addAll(int index, Collection<? extends TYPE> c) {
        if (c != null) {
            for (TYPE it : c) {
                add(index, it);
                index++;
            }
            return c.size() > 0;
        }
        return false;
    }

    public TYPE get(int index) {
        return list.get(index);
    }

    public TYPE set(int index, TYPE element) {
        onAdd(element);
        TYPE it = list.get(index);
        String key = makeKey(it.getName());
        String newName = makeKey(element.getName());
        if (!key.equals(newName) && map.containsKey(newName)) {
            throw new XError("Duplicate name [{0}] in NamedList", newName);
        }
        map.remove(key);
        map.put(newName, element);
        list.set(index, element);
        onRemove(it);
        return it;
    }

    public void add(int index, TYPE it) {
        onAdd(it);
        String key = makeKey(it.getName());
        if (map.containsKey(key)) {
            TYPE itPrev = map.remove(key);
            int idx = list.indexOf(itPrev);
            map.put(key, it);
            list.remove(idx);
            list.add(idx, it);
            onRemove(itPrev);
        } else {
            map.put(key, it);
            list.add(index, it);
        }
    }

    public TYPE remove(int index) {
        TYPE it = list.remove(index);
        String key = makeKey(it.getName());
        map.remove(key);
        onRemove(it);
        return it;
    }

    public int indexOf(Object o) {
        if (o instanceof CharSequence) {
            String key = makeKey(o);
            TYPE it = map.get(key);
            if (it == null) {
                return -1;
            }
            o = it;
        }
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

    public ListIterator<TYPE> listIterator() {
        return list.listIterator();
    }

    public ListIterator<TYPE> listIterator(int index) {
        return list.listIterator(index);
    }

    public List<TYPE> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
