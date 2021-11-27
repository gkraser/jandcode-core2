package jandcode.commons.reflect.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.reflect.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class ReflectTableHolderImpl {

    protected Map<Class, ReflectTable> items = new ConcurrentHashMap<>();

    public ReflectTable getItem(Class cls) {
        if (cls == null) {
            throw new XError("cls is null");
        }
        ReflectTable res = items.get(cls);
        if (res == null) {
            synchronized (this) {
                res = items.get(cls);
                if (res == null) {
                    res = makeItem(cls);
                    items.put(cls, res);
                }
            }
        }
        return res;
    }

    private ReflectTable makeItem(Class cls) {
        Map<String, ReflectTableFieldImpl> fields = new LinkedHashMap<>();
        List<Method> setters = new ArrayList<>();

        // собираем все публичные getters, они определяют состав полей
        // заодно собираем более-менее похожие на правильные setters
        for (Method m : cls.getMethods()) {

            // статические пропускаем
            if (Modifier.isStatic(m.getModifiers())) {
                continue;
            }
            // не публичные пропускаем
            if (!Modifier.isPublic(m.getModifiers())) {
                continue;
            }

            String nm = m.getName();

            if (nm.startsWith("get")) {
                // системные пропускаем
                if (nm.equals("getClass") || nm.equals("getMetaClass")) {
                    continue;
                }

                // пропускаем просто get
                if (nm.length() <= 3) {
                    continue;
                }
                // пропускаем с параметрами
                if (m.getParameterCount() > 0) {
                    continue;
                }
                // пропускаем без возвращаемого значения
                if (m.getReturnType() == void.class) {
                    continue;
                }

                String fieldName = UtString.uncapFirst(nm.substring(3));
                ReflectTableFieldImpl field = fields.get(fieldName);
                // если не определен, создаем
                if (field == null) {
                    field = new ReflectTableFieldImpl();
                    field.setName(fieldName);
                    field.setGetter(m);
                    field.setType(m.getReturnType());
                    fields.put(fieldName, field);
                }

            } else if (nm.startsWith("set")) {
                // пропускаем просто set
                if (nm.length() <= 3) {
                    continue;
                }
                // пропускаем с неправильными параметрами
                if (m.getParameterCount() != 1) {
                    continue;
                }
                // пропускаем без возвращаемого значения
                if (m.getReturnType() == void.class) {
                    continue;
                }

                setters.add(m);
            }

        }

        // setters настраиваем
        for (Method m : setters) {
            String fieldName = m.getName().substring(3);
            ReflectTableFieldImpl field = fields.get(fieldName);
            if (field != null) {
                if (field.getType().isAssignableFrom(m.getReturnType())) {
                    // правильный тип параметра
                    field.setSetter(m);
                }
            }
        }

        // и поля, включая приватные
        grabFields(cls, fields);

        return new ReflectTableImpl(cls, fields.values());
    }

    private void grabFields(Class cls, Map<String, ReflectTableFieldImpl> fields) {
        if (cls == null || cls == Object.class) {
            return;
        }
        for (Field f : cls.getDeclaredFields()) {
            ReflectTableFieldImpl tf = fields.get(f.getName());
            if (tf != null) {
                if (tf.getField() == null) {
                    tf.setField(f);
                }
            }
        }
        grabFields(cls.getSuperclass(), fields);
    }

}
