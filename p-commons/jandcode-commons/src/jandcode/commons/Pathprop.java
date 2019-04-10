package jandcode.commons;

import jandcode.commons.io.*;

import java.io.*;
import java.util.*;

/**
 * Свойства каталога.
 * <p>
 * Свойства каталога определяются в файле '.pathprop'. Его формат - properties.
 * При получении значения свойства для определенного каталога анализируются файлы
 * '.pathprop' начиная с указанного каталога и вверх по иерархии каталогов.
 * Первое найденное значение и является значением свойства.
 * <p>
 * В значениях свойств допустимы подстановки:
 * <p>
 * ${path} - заменяется на полный путь к каталогу, в котором лежит файл .pathprop.
 * <p>
 * Если в каталоге присутсвует файл _.pathprop, то он так же загружается и накладывается
 * на .pathprop. Используется для перекрытия свойств для конкретного пользователя.
 */
public class Pathprop {

    /**
     * Имя файла со свойствами путей
     */
    public final static String PATHPROP_FILE = ".pathprop"; //NON-NLS

    /**
     * Имя файла со свойствами путей накладываемый на .pathprop.
     * Для перекрытия свойств.
     */
    public final static String PATHPROP_FILE_OVERRIDE = "_.pathprop"; //NON-NLS

    private static Map<String, String> EMPTY_PROPS = Collections.unmodifiableMap(new HashMap<String, String>());

    private HashMap<String, Map<String, String>> _pathprop = new HashMap<String, Map<String, String>>();
    private static Pathprop inst = new Pathprop();

    /**
     * properties cо сборкой ключей в порядке их появления в файле
     */
    class PropertiesWrap extends Properties {
        private Map<String, String> map = new LinkedHashMap<>();

        public Map<String, String> getMap() {
            return map;
        }

        public synchronized Object put(Object key, Object value) {
            map.put(UtString.toString(key), UtString.toString(value));
            return super.put(key, value);
        }
    }

    /**
     * Глобальный экземпляр
     */
    public static Pathprop getInst() {
        return inst;
    }

    /**
     * Значение свойства для каталога
     *
     * @param propName какое свойство
     * @param path     для какого каталога
     * @return значение свойства. Если нет свойства - пустая строка.
     */
    public String getPathprop(String propName, String path) {
        File f = new File(UtFile.abs(path));
        while (f != null) {
            Map<String, String> p = getProps(f.getAbsolutePath());
            String v = p.get(propName);
            if (v != null) {
                return v;
            }
            f = f.getParentFile();
        }
        return "";
    }

    /**
     * Возвращает список всех свойств и их значений из файлов .pathprop
     * начиная с каталога path и вверх. Порядок свойств от верхних к нижним.
     *
     * @see UtFile#getPathprop(java.lang.String, java.lang.String)
     */
    public Map<String, String> getPathprops(String path) {
        Map<String, String> res = new LinkedHashMap<String, String>();
        File f = new File(UtFile.abs(path));
        internalGetPathprops2(f, res);
        return res;
    }

    private void internalGetPathprops2(File f, Map<String, String> res) {
        Map<String, String> p = getProps(f.getAbsolutePath());
        for (String k : p.keySet()) {
            String v = p.get(k);
            if (!res.containsKey(k)) {
                res.put(k, v);
            }
        }
        File f1 = f.getParentFile();
        if (f1 != null) {
            internalGetPathprops2(f1, res);
        }
    }

    //////

    /**
     * Получить свойства из указанного каталога
     *
     * @param path абсолютный путь к каталогу
     * @return свойства этого каталога. Всегда не null, даже если нет файла.
     */
    protected Map<String, String> getProps(String path) {
        path = UtFile.abs(path);
        Map<String, String> p = _pathprop.get(path);
        if (p == null) {
            synchronized (this) {
                p = _pathprop.get(path);
                if (p == null) {
                    p = loadProps(path);
                    _pathprop.put(path, p);
                }
            }
        }
        return p;
    }

    /**
     * Загрузить свойства из указанного каталога
     *
     * @param absPath абсолютный путь к каталогу
     * @return свойства этого каталога. Всегда не null, даже если нет файла.
     */
    protected Map<String, String> loadProps(final String absPath) {
        Map<String, String> m1 = loadFile(UtFile.join(absPath, PATHPROP_FILE));
        Map<String, String> m2 = loadFile(UtFile.join(absPath, PATHPROP_FILE_OVERRIDE));

        if (m1 == null && m2 == null) {
            return EMPTY_PROPS;
        }
        if (m1 != null && m2 != null) {
            m1.putAll(m2);
            return m1;
        }
        if (m1 != null) {
            return m1;
        } else {
            return m2;
        }
    }

    /**
     * Загрузить свойства из указанного файла
     *
     * @param fn абсолютный путь к файлу .pathprop
     * @return содержимое файлп или null, если файла нет
     */
    protected Map<String, String> loadFile(final String fn) {
        if (!UtFile.exists(fn)) {
            return null;
        }

        PropertiesWrap pp = new PropertiesWrap();
        try {
            new PropertiesLoader(pp).load().fromFile(fn);
        } catch (Exception e) {
            return null;
        }

        class Subst implements ISubstVar {
            public String onSubstVar(String v) {
                if (v.equals("path")) {
                    return UtFile.path(fn);
                }
                return "";
            }
        }

        Subst subst = new Subst();
        Map<String, String> res = new LinkedHashMap<String, String>();
        for (Object o : pp.getMap().keySet()) {
            String key = UtString.toString(o);
            String v = UtString.toString(pp.getMap().get(key));
            v = UtString.substVar(v, subst);
            v = v.trim();
            res.put(key, v);
        }

        return res;
    }


}
