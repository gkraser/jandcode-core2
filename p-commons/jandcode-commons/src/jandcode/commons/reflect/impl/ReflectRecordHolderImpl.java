package jandcode.commons.reflect.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.reflect.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class ReflectRecordHolderImpl {

    protected Map<Class, ReflectRecord> items = new ConcurrentHashMap<>();

    public ReflectRecord getItem(Class cls) {
        if (cls == null) {
            throw new XError("cls is null");
        }
        ReflectRecord res = items.get(cls);
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

    private ReflectRecord makeItem(Class cls) {
        Map<String, ReflectRecordFieldImpl> fields = new LinkedHashMap<>();
        List<Method> setters = new ArrayList<>();

        // собираем gettres/setters и создаем поля
        grab(cls, fields, setters);

        // setters настраиваем
        for (Method m : setters) {
            String fieldName = m.getName().substring(3);
            ReflectRecordFieldImpl field = fields.get(fieldName);
            if (field != null) {
                if (field.getType().isAssignableFrom(m.getReturnType())) {
                    // правильный тип параметра
                    field.setSetter(m);
                }
            }
        }

        // и поля, включая приватные
        grabFields(cls, fields);

        // собираем веса полей (декларативный порядок)
        Map<String, Integer> fieldsWeight = new HashMap<>();
        grabFieldsWeight(cls, fieldsWeight);
        List<ReflectRecordFieldImpl> fieldsList = new ArrayList<>(fields.values());
        for (ReflectRecordFieldImpl f : fieldsList) {
            Integer weight = fieldsWeight.get(f.getName());
            if (weight == null) {
                weight = 1000000;
            }
            f.setWeight(weight);
        }
        // сортируем в правильном порядке
        fieldsList.sort((o1, o2) -> {
            Integer i1 = o1.getWeight();
            Integer i2 = o2.getWeight();
            int n = i1.compareTo(i2);
            if (n == 0) {
                String s1 = o1.getName();
                String s2 = o2.getName();
                n = s1.compareToIgnoreCase(s2);
            }
            return n;
        });

        // готово
        return new ReflectRecordImpl(cls, fieldsList);
    }

    private void grab(Class cls, Map<String, ReflectRecordFieldImpl> fields, List<Method> setters) {
        if (cls == null || cls == Object.class) {
            return;
        }
        // собираем все публичные getters, они определяют состав полей
        // заодно собираем более-менее похожие на правильные setters
        for (Method m : cls.getDeclaredMethods()) {

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
                ReflectRecordFieldImpl field = fields.get(fieldName);
                // если не определен, создаем
                if (field == null) {
                    field = new ReflectRecordFieldImpl();
                    field.setName(fieldName);
                    field.setGetter(m);
                    field.setType(m.getReturnType());
                    fields.put(fieldName, field);
                }
                // свойства из аннотации
                grabProps(field, m.getAnnotation(FieldProps.class));

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
        grab(cls.getSuperclass(), fields, setters);
    }

    private void grabFields(Class cls, Map<String, ReflectRecordFieldImpl> fields) {
        if (cls == null || cls == Object.class) {
            return;
        }
        for (Field f : cls.getDeclaredFields()) {
            ReflectRecordFieldImpl tf = fields.get(f.getName());
            if (tf != null) {
                if (tf.getField() == null) {
                    tf.setField(f);
                }
                grabProps(tf, f.getAnnotation(FieldProps.class));
            }
        }
        grabFields(cls.getSuperclass(), fields);
    }

    private void grabProps(ReflectRecordFieldImpl field, FieldProps fieldProps) {
        if (fieldProps == null) {
            return;
        }

        if (!UtString.empty(fieldProps.dict())) {
            field.setPropIfNotExist("dict", fieldProps.dict());
        }
        if (fieldProps.size() > 0) {
            field.setPropIfNotExist("size", fieldProps.size());
        }
    }

    /**
     * Собираем декларативный порядок определеня полей
     */
    private void grabFieldsWeight(Class cls, Map<String, Integer> fieldsWeight) {
        if (cls == null || cls == Object.class) {
            return;
        }
        // сначала у предка
        grabFieldsWeight(cls.getSuperclass(), fieldsWeight);

        // потом у себя
        for (Field f : cls.getDeclaredFields()) {
            Integer ord = fieldsWeight.get(f.getName());
            if (ord == null) {
                fieldsWeight.put(f.getName(), fieldsWeight.size() + 1);
            }
        }
    }
}
