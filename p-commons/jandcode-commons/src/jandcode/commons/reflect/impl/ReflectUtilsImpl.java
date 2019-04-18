package jandcode.commons.reflect.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.reflect.*;
import jandcode.commons.reflect.impl.convs.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class ReflectUtilsImpl implements ReflectUtils {

    private HashMap<Class, Clazz> classes = new HashMap<>();
    private HashMap<Class, ReflectPropConvertor> convertors = new HashMap<>();
    private HashSet<String> scannedPackages = new HashSet<>();
    private HashMap<Class, Class> defaultImpls = new HashMap<>();


    /**
     * Метод для установки значения.
     * Может быть:
     * setXXX(Object) - обычный setter. Требуется конвертор.
     * setXXXName(String) - обычный setter для свойства XXX в виде строки. Не требуется конвертор.
     */
    class Setter {

        /**
         * Имя элемента (lowercase)
         */
        String name;

        /**
         * Конвертор значений
         */
        ReflectPropConvertor convertor;

        /**
         * Метод для установки значения
         */
        Method method;

        /**
         * Класс параметра
         */
        Class param;
    }

    /**
     * Метод для получения значения.
     * Может быть:
     * getXXX() - обычный getter
     */
    class Getter {

        /**
         * Имя элемента (lowercase)
         */
        String name;

        /**
         * Метод для получения значения
         */
        Method method;

    }


    public class Clazz implements ReflectClazz {

        Class clazz;
        HashMap<String, Setter> setters = new HashMap<>();
        HashMap<String, Getter> getters = new HashMap<>();
        private String packageName;
        boolean unknownSetter;

        public Clazz(Class clazz) {
            this.clazz = clazz;
            this.unknownSetter = IReflectUnknownSetter.class.isAssignableFrom(this.clazz);
            addIgnoreSetter("name");
            addIgnoreSetter("parent");
            addIgnoreSetter("class");
            addIgnoreSetter("abstract");
            addIgnoreSetter("comment");
            addIgnoreSetter("text");
            addIgnoreSetter("override");
            addIgnoreSetter("extends");
        }

        public void invokeSetter(Object inst, String name, Object value) {
            Setter setr = setters.get(name.toLowerCase());
            if (setr == null) {
                if (this.unknownSetter) {
                    ((IReflectUnknownSetter) inst).onUnknownSetter(name, value);
                }
                return;
            }

            if (setr.method == null) {
                return; // ignore
            }

            try {
                Object v = value;
                if (v instanceof CharSequence) {
                    v = value.toString();
                }
                if (v instanceof String) {
                    if (setr.convertor != null) {
                        // конвертор есть, присваиваем
                        v = setr.convertor.fromString((String) v);
                        setr.method.invoke(inst, v);
                        return;
                    } else {
                        // присваиваем строку, но конвертора нет
                        if (this.unknownSetter) {
                            ((IReflectUnknownSetter) inst).onUnknownSetter(name, value);
                        }
                        return;
                    }
                }
                // конвертора нет, присваиваем не строку
                if (setr.param.isPrimitive()) {
                    setr.method.invoke(inst, v);
                } else if (v != null && setr.param != null && setr.param.isAssignableFrom(v.getClass())) {
                    setr.method.invoke(inst, v);
                }
            } catch (IllegalAccessException e) {
                throw new XErrorWrap(e.getCause());
            } catch (InvocationTargetException e) {
                throw new XErrorWrap(e);
            }

        }

        public Object invokeGetter(Object inst, String name) {
            Getter getr = getters.get(name.toLowerCase());
            if (getr == null) {
                return null;
            }

            try {
                return getr.method.invoke(inst);
            } catch (IllegalAccessException e) {
                throw new XErrorWrap(e.getCause());
            } catch (InvocationTargetException e) {
                throw new XErrorWrap(e);
            }

        }

        void addSetter(Setter st) {
            st.name = st.name.toLowerCase();
            Setter exists = setters.get(st.name);
            if (exists != null && exists.convertor != null &&
                    exists.convertor.getClass() == PropConvertorString.class) {
                return; // отдаем преимущества для строковых setter
            }
            if (st.method != null) {
                st.method.setAccessible(true);
            }
            setters.put(st.name, st);
        }

        void addGetter(Getter gt) {
            gt.name = gt.name.toLowerCase();
            if (gt.method != null) {
                gt.method.setAccessible(true);
            }
            getters.put(gt.name, gt);
        }


        void addIgnoreSetter(String name) {
            Setter a = new Setter();
            a.name = name.toLowerCase();
            setters.put(a.name, a);
        }

        public Class getCls() {
            return clazz;
        }
    }


    public ReflectUtilsImpl() {
        convertors.put(int.class, new PropConvertorInteger());
        convertors.put(long.class, new PropConvertorLong());
        convertors.put(boolean.class, new PropConvertorBoolean());
        convertors.put(double.class, new PropConvertorDouble());
        convertors.put(char.class, new PropConvertorChar());
        convertors.put(Integer.class, new PropConvertorInteger());
        convertors.put(Long.class, new PropConvertorLong());
        convertors.put(Double.class, new PropConvertorDouble());
        convertors.put(Boolean.class, new PropConvertorBoolean());
        convertors.put(String.class, new PropConvertorString());
        convertors.put(Character.class, new PropConvertorChar());
        //!! зачем? !! // convertors.put(Object.class, new PropConvertorString());
        convertors.put(Class.class, new PropConvertorClass());
        convertors.put(File.class, new PropConvertorFile());
    }

    public Clazz getClazz(Class c) {
        Clazz cls = classes.get(c);
        if (cls == null) {
            try {
                cls = registerClass(c);
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return cls;
    }

    protected Clazz registerClass(Class c) throws Exception {
        Clazz cls;
        synchronized (this) {
            cls = classes.get(c);
            if (cls != null) {
                // уже был зарегистрирован в другом потоке
                return cls;
            }
            // создаем новую конфигурацию
            cls = createClazz(c);
            Package pak = c.getPackage();
            if (pak != null) {
                cls.packageName = pak.getName();
            } else {
                cls.packageName = "";
            }
            // запоминаем
            classes.put(c, cls);
            return cls;
        }
    }

    private Clazz createClazz(Class c) throws Exception {
        Clazz cls = new Clazz(c);

        while (c != null && c != Object.class) {
            Method[] meth = c.getDeclaredMethods();
            for (Method m : meth) {
                addSetter(cls, m);
                addGetter(cls, m);
            }
            c = c.getSuperclass();
        }

        return cls;
    }

    private void errorMethod(ReflectProp anProp, Method m, String msg) {
        if (anProp == null) {
            return;
        }
        String s = "Ошибка в описании метода [" + m.toString() + "] помеченного атрибутом ReflectProp";
        s = s + ": " + msg;
        throw new XError(s);
    }

    /**
     * Добавить сеттер, если метод - сеттер
     */
    private void addSetter(Clazz cls, Method m) throws Exception {
        ReflectProp anProp = m.getAnnotation(ReflectProp.class);

        String mname = m.getName();

        if (mname.length() < 4) {
            errorMethod(anProp, m, "Имя метода меньше 4 симолов");
            return;
        }

        int md = m.getModifiers();
        if (Modifier.isPrivate(md)) {
            errorMethod(anProp, m, "Метод должен быть public или protected");
            return;
        }

        Setter st = new Setter();
        st.name = mname.substring(3);
        st.method = m;

        Class<?>[] mParams = m.getParameterTypes();
        if (mParams.length != 1) {
            errorMethod(anProp, m, "У метода должен быть один параметр");
            return;
        }
        st.param = mParams[0];

        if (anProp != null) {
            if (anProp.name().length() > 0) {
                st.name = anProp.name();
            }
            st.convertor = getConvertor(anProp.convertor());
        } else {
            if (!mname.startsWith("set")) {
                return;
            }
        }

        if (st.convertor == null) {
            st.convertor = getConvertor(mParams[0]);
            if (st.convertor == null) {
                errorMethod(anProp, m, "Тип параметра не совместим. Определите конвертор");
                // если errorMethod не вывалился, то добавляем без конвертора
                // можно будет использовать в asis присвоениях
                cls.addSetter(st);
                return; // неизвестный тип параметра
            }
        }

        if (st.name.endsWith("Name") && mParams[0] == String.class && !st.name.equals("Name")) {
            // для setXxxName(String) добавляем дополнительный обработчик
            Setter stn = new Setter();
            stn.name = st.name.substring(0, st.name.length() - 4);
            stn.method = m;
            stn.convertor = st.convertor;
            stn.param = String.class;
            cls.addSetter(stn);
        }

        cls.addSetter(st);
    }

    /**
     * Добавить геттер, если метод - геттер
     */
    private void addGetter(Clazz cls, Method m) throws Exception {
        String mname = m.getName();

        if (mname.length() < 4) {
            return;
        }

        if (!mname.startsWith("get")) {
            return;
        }

        int md = m.getModifiers();
        if (Modifier.isPrivate(md)) {
            return;
        }

        Class<?>[] mParams = m.getParameterTypes();
        if (mParams.length != 0) {
            return;
        }

        Getter gt = new Getter();
        gt.name = mname.substring(3);
        gt.method = m;

        cls.addGetter(gt);
    }

    /**
     * Получить конвертор
     *
     * @param aClass или класс конвертора или класс параметра
     * @return конвертор
     */
    private ReflectPropConvertor getConvertor(Class aClass) throws Exception {
        if (aClass == ReflectProp.class) {
            return null;
        }
        ReflectPropConvertor res = convertors.get(aClass);
        if (res != null) {
            return res;
        }
        if (!ReflectPropConvertor.class.isAssignableFrom(aClass)) {
            return null;
        }
        res = (ReflectPropConvertor) aClass.newInstance();
        convertors.put(aClass, res);
        return res;
    }

    ////// bean interface  ///////

    private void scanPackage(String packageName) {
        if (!scannedPackages.contains(packageName)) {
            synchronized (this) {
                if (scannedPackages.contains(packageName)) {
                    return;
                }
                List<Class> lst;
                ClassLoader saveldr = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                try {
                    lst = UtClass.grabPublic(packageName);
                } finally {
                    Thread.currentThread().setContextClassLoader(saveldr);
                }
                for (Class aClass : lst) {
                    getClazz(aClass);
                }
                scannedPackages.add(packageName);
            }
        }
    }

    public List<ReflectClazz> list(String packageName, boolean recursive) {
        scanPackage(packageName);
        ArrayList<ReflectClazz> res = new ArrayList<>();
        String pns = packageName + ".";
        for (Clazz clazz : classes.values()) {
            if (recursive) {
                if (packageName.equals(clazz.packageName) || clazz.packageName.startsWith(pns)) {
                    res.add(clazz);
                }
            } else {
                if (packageName.equals(clazz.packageName)) {
                    res.add(clazz);
                }
            }
        }
        return res;
    }

    public void setProps(Object inst, Map props) {
        if (props != null && !props.isEmpty()) {
            ReflectClazz cz = getClazz(inst.getClass());
            for (Object key : props.keySet()) {
                String an = key.toString();
                if (an.startsWith("_")) {
                    continue;
                }
                Object av = props.get(key);
                cz.invokeSetter(inst, an, av);
            }
        }
    }

    //////

    public Class getDefaultImplementation(Class cls) {
        Class a = defaultImpls.get(cls);
        if (a == null) {
            synchronized (this) {
                a = defaultImpls.get(cls);
                if (a == null) {
                    DefaultImplementation ann = (DefaultImplementation) cls.getAnnotation(DefaultImplementation.class);
                    if (ann != null) {
                        a = ann.value();
                    } else {
                        a = cls;
                    }
                    defaultImpls.put(cls, a);
                }
            }
        }
        return a;
    }

}