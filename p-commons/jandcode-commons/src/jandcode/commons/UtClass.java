package jandcode.commons;

import jandcode.commons.error.*;
import org.apache.commons.vfs2.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

/**
 * Утилиты для классов
 */
@SuppressWarnings("unchecked")
public class UtClass {

    /**
     * Кеш уже добавленных путей в classpath (через addClassPath)
     */
    private static HashSet<String> classpathCache = new HashSet<String>();

    /**
     * ClassLoader наследник от {@link URLClassLoader}.
     * Этот ClassLoader нужен для динамического добавления элементов в
     * classpath в java>=9.
     */
    public static class JcURLClassLoader extends URLClassLoader {

        public JcURLClassLoader(ClassLoader parent) {
            super(new URL[]{}, parent);
        }

        public void addURL(URL url) {
            super.addURL(url);
        }

    }

    /**
     * Метод фиксит проблему динамического добавления элементов в classpath в java>=9.
     * Для этого создается новый ClassLoader, наследник от {@link URLClassLoader}
     * и устанавливается как "context class loader".
     * Этот метод нужно вызывать наиболее близко к началу выполнения кода,
     * который потенциально будет изменять classpath в runtime.
     */
    public static void fixAddClasspath() {
        ClassLoader curLdr = Thread.currentThread().getContextClassLoader();
        if (!(curLdr instanceof JcURLClassLoader)) {
            JcURLClassLoader newLdr = new JcURLClassLoader(curLdr);
            Thread.currentThread().setContextClassLoader(newLdr);
        }
    }

    /**
     * Возвращает {@link ClassLoader} Thread.currentThread().getContextClassLoader().
     * Используется для получения ClassLoader в рамках соглашения в jandcode.
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Сбор классов в указанных packages.
     * Сканируются: public, !abstract, !member
     *
     * @param packages пакеты
     * @return список найденных классов
     */
    public static List<Class> grabPublic(String[] packages) {
        ArrayList<Class> res = new ArrayList<Class>();

        if (packages == null) {
            return res;
        }

        for (String pn : packages) {
            List<Class> fnd = findClassesInPackage(pn, null);
            //
            for (Class type : fnd) {
                if (type.isMemberClass()) {
                    continue;
                }
                int md = type.getModifiers();
                if (Modifier.isAbstract(md) || (!Modifier.isPublic(md))) {
                    continue;
                }
                ///
                res.add(type);
            }
            //
        }

        return res;
    }

    /**
     * Сбор классов в указанных packages.
     *
     * @param packages пакеты через ','
     * @see UtClass#grabPublic(java.lang.String[])
     */
    public static List<Class> grabPublic(String packages) {
        if (UtString.empty(packages)) {
            return new ArrayList<Class>();
        }
        return grabPublic(packages.split(","));
    }

    /**
     * Сбор классов в указанных packages.
     *
     * @param packages пакеты через ','
     * @see UtClass#grabPublic(java.lang.String[])
     */
    public static List<Class> grabPublic(List packages) {
        return grabPublic(UtString.toArray(packages));
    }

    //////

    /**
     * Создать такой же экземпляр. Свойства не инициализируются.
     *
     * @param prototype прототип. Создается экземпляр такого же класса
     * @return новый экземпляр
     */
    public static Object createSameInst(Object prototype) {
        if (prototype == null) {
            throw new RuntimeException(UtLang.t("Прототип объекта не может быть null"));
        }
        try {
            Object res = prototype.getClass().newInstance();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создать экземпляр объекта
     *
     * @param clazz класс
     * @return новый экземпляр
     */
    public static Object createInst(Class clazz) {
        if (clazz == null) {
            throw new RuntimeException(UtLang.t("Класс объекта не может быть null"));
        }
        try {
            Object res = clazz.newInstance();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Выполнение поиска класса по имени через {@code Class.forName}
     *
     * @param className имя класса
     * @return класс
     * @throws ClassNotFoundException
     */
    public static Class classForName(String className) throws ClassNotFoundException {
        try {
            return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            if ("int".equals(className)) return int.class;
            if ("long".equals(className)) return long.class; //NON-NLS
            if ("double".equals(className)) return double.class; //NON-NLS
            if ("boolean".equals(className)) return boolean.class; //NON-NLS
            if ("char".equals(className)) return char.class; //NON-NLS
            if ("byte".equals(className)) return byte.class; //NON-NLS
            if ("byte[]".equals(className)) return byte[].class; //NON-NLS
            if ("byte-array".equals(className)) return byte[].class; //NON-NLS
            if ("short".equals(className)) return short.class; //NON-NLS
            if ("void".equals(className)) return void.class; //NON-NLS
            throw e;
        }
    }

    /**
     * Создать экземпляр объекта
     *
     * @param clazzName имя класса
     * @return новый экземпляр
     */
    public static Object createInst(String clazzName) {
        if (UtString.empty(clazzName)) {
            throw new RuntimeException(UtLang.t("Имя класса не может быть пустым"));
        }
        try {
            Class clazz = getClass(clazzName);
            Object res = clazz.newInstance();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить класс по имени.
     *
     * @param className имя класса. Может получать вложенные классы, даже если в имени '$'
     *                  заменен на '.'
     * @return класс с именем. Если не найден - ошибка.
     */
    public static Class getClass(String className) {

        try {
            return classForName(className);
        } catch (ClassNotFoundException e) {
        }

        StringBuilder sb = new StringBuilder(className);
        int n = sb.length() - 1;
        while (n >= 0) {
            if (sb.charAt(n) == '.') {
                sb.setCharAt(n, '$');
                try {
                    return classForName(sb.toString());
                } catch (ClassNotFoundException e) {
                }
            }
            n--;
        }

        throw new RuntimeException(UtLang.t("Не найден класс [{0}]", className));
    }

    /**
     * Проверка на публичный класс: public, !abstract, !member
     */
    public static boolean isPublic(Class type) {
        if (type.isMemberClass()) {
            return false;
        }
        int md = type.getModifiers();
        if (Modifier.isAbstract(md) || (!Modifier.isPublic(md))) {
            return false;
        }
        return true;
    }

    /**
     * Добавить путь к текущему classpath
     *
     * @param path какой путь
     * @return true, если путь был добавлен. false - если уже был добален ранее
     */
    public static boolean addClasspath(String path) {
        if (UtString.empty(path)) {
            return false;
        }
        //
        try {
            File f = new File(path);
            URL u = f.toURI().toURL();
            String us = u.toString();
            if (classpathCache.contains(us)) {
                return false;
            }
            //
            ClassLoader curLdr = Thread.currentThread().getContextClassLoader();
            if (!(curLdr instanceof JcURLClassLoader)) {
                fixAddClasspath();
            }
            JcURLClassLoader jcLdr = (JcURLClassLoader) Thread.currentThread().getContextClassLoader();
            jcLdr.addURL(u);
            //
            classpathCache.add(us);
            //
            return true;
            //
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает список из всех classpath на момент вызова
     */
    public static List<String> getClasspath() {
        List<String> res = new ArrayList<>();

        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        for (URL u : urlClassLoader.getURLs()) {
            String p = u.getPath();
            if (UtString.empty(p)) {
                continue;
            }
            String fn = new File(p).getAbsolutePath();
            res.add(fn);
        }
        return res;
    }

    /**
     * Поиск метода по имени без учета параметров
     *
     * @param cls        класс
     * @param methodName имя метода
     * @return метод или null, если не найден
     */
    public static Method findMethod(Class cls, String methodName) {
        for (Method method : cls.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * Получить цепочку наследовани начиная с from до to. from - младше!
     * <code>getInheritedList(Object.class, MyClass.class)</code>
     */
    public static List<Class> getInheritedList(Class from, Class to) {
        List<Class> res = new ArrayList<Class>();
        internal_getInheritedList(res, to, from);
        return res;
    }

    private static void internal_getInheritedList(List<Class> res, Class from, Class to) {
        if (from == null) {
            return;
        }
        if (from != to) {
            internal_getInheritedList(res, from.getSuperclass(), to);
        }
        res.add(from);
    }

    //////

    /**
     * Поиск классов в указанном пакете с учетом classpath
     */
    public static List<Class> findClassesInPackage(String packageName, ClassLoader loader) {
        LinkedHashSet<Class> res = new LinkedHashSet<Class>();
        //
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }

        internal_findClassesInPackage(res, packageName, loader);

        List<Class> res2 = new ArrayList<Class>();
        res2.addAll(res);

        return res2;
    }

    /**
     * Поиск классов в указанном пакете с учетом classpath
     */
    private static void internal_findClassesInPackage(Set<Class> res, String packageName, ClassLoader loader) {
        //
        String packagePath = packageName.replace('.', '/');
        Enumeration<URL> urls;

        try {
            urls = loader.getResources(packagePath);
        } catch (IOException ioe) {
            return;
        }

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            FileObject vf = UtFile.getFileObject(url.toString());

            FileObject[] childs;
            try {
                childs = vf.getChildren();
                for (FileObject ch : childs) {
                    if (ch.getType() == FileType.FOLDER) {
                        internal_findClassesInPackage(res, packageName + "." + ch.getName().getBaseName(), loader);
                    } else {
                        String ext = ch.getName().getExtension();
                        if (!"class".equals(ext)) {
                            continue;
                        }
                        String fn = UtFile.removeExt(ch.getName().getBaseName());
                        Class cls = loader.loadClass(packageName + "." + fn);
                        res.add(cls);
                    }
                }
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }

    }

}
