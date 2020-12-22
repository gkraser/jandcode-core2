package jandcode.commons;

import jandcode.commons.conf.*;
import jandcode.commons.conf.impl.*;
import jandcode.commons.io.*;

import java.util.*;

/**
 * Статические утилиты для Conf
 */
public class UtConf {

    /**
     * Создать экземпляр Conf
     */
    public static Conf create() {
        return new ConfImpl(null);
    }

    /**
     * Создать поименнованный экземпляр Conf
     */
    public static Conf create(String name) {
        return new ConfImpl(name);
    }

    /**
     * Создать экземпляр Conf из Conf, Map, Collection
     */
    public static Conf create(Object data) {
        Conf x = new ConfImpl(null);
        x.join(data);
        return x;
    }

    //////

    /**
     * Создать загрузчик
     */
    public static ConfLoader createLoader(Conf x) {
        return new ConfLoaderImpl(x);
    }

    /**
     * Загрузить
     */
    public static LoadFrom load(Conf x) {
        return new ConfLoaderImpl(x).load();
    }

    /**
     * Сохранить в xml
     */
    public static SaveTo save(Conf x) {
        return new ConfXmlSaver(x).save();
    }

    //////

    /**
     * Создать expander
     */
    public static ConfExpander createExpander(Conf x) {
        return new ConfExpanderImpl(x);
    }

    //////

    /**
     * Сконверитровать в Map. Пригоден для трансляции в json.
     */
    public static Map<String, Object> toMap(Conf x) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (String pn : x.keySet()) {
            Object v = x.getValue(pn);
            if (v instanceof Conf) {
                m.put(pn, toMap((Conf) v));
            } else {
                m.put(pn, v.toString());
            }
        }
        return m;
    }

    /**
     * Список всех используемых свойств в конфикурации.
     * Вложенные свойства разделяются '/'
     *
     * @param x           конфигурация
     * @param includeConf true - включать свойства, которые имеют тип Conf.
     * @return список свойств
     */
    public static List<String> getPropNames(Conf x, boolean includeConf) {
        List<String> res = new ArrayList<>();
        if (x != null) {
            internal_getPropNames(res, x, includeConf, "");
        }
        return res;
    }

    /**
     * Список всех используемых свойств в конфикурации.
     * Вложенные свойства разделяются '/'. Свойства типа Conf пропускаются.
     *
     * @param x конфигурация
     * @return список свойств
     */
    public static List<String> getPropNames(Conf x) {
        return getPropNames(x, false);
    }

    private static void internal_getPropNames(List<String> res, Conf x, boolean includeConf, String prefix) {
        for (Map.Entry<String, Object> en : x.entrySet()) {
            String k = en.getKey();
            Object v = en.getValue();
            if (v instanceof Conf) {
                if (includeConf) {
                    res.add(prefix + k);
                }
                internal_getPropNames(res, (Conf) v, includeConf, prefix + k + "/");
            } else {
                res.add(prefix + k);
            }
        }
    }

    //////

    /**
     * Нормализация имени. Заменяет '--','/',':' на '!'
     */
    public static String normalizeName(String name) {
        if (name == null) {
            return ConfConsts.NONAME_KEY;
        }
        if (name.indexOf('-') != -1) {
            name = name.replace("--", "!");
        }
        if (name.indexOf('/') != -1) {
            name = name.replace('/', '!');
        }
        while (name.contains(":")) {
            name = name.replace(':', '!');
        }
        while (name.startsWith("!")) {
            name = name.substring(1);
        }
        while (name.endsWith("!")) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    /**
     * Сортировка списка Conf по весу.
     * <p>
     * В src имеем исходный список Conf. В ord имеем Conf с такими же именами, как и в src,
     * с атрибутами - weight (вес). Чем больше вес, тем ниже будет
     * расположен элемент с соответсвующим именем из src в результирующем списке.
     * <p>
     * Если weight не указан - принимается равным 50. Если нет элемента в ord,
     * то принимается его вес 50. Элементы с одинаковым весом располагаются
     * в порядке, определенном в списке src.
     *
     * @param src исходный список
     * @param ord список с определением порядка. Может совпадать с src
     * @return новый список элементов из src в правильном порядке
     */
    public static List<Conf> sortByWeight(Collection<Conf> src, Collection<Conf> ord) {
        List<Conf> res = new ArrayList<>();
        List<ConfWeightItem> tmp = new ArrayList<>();

        // нет исходных, возвращаем пустой
        if (src == null || src.size() == 0) {
            return res;
        }

        // нет порядка, возвращаем исходный
        if (ord == null || ord.size() == 0) {
            res.addAll(src);
            return res;
        }

        // из ord делаем map по имени
        Map<String, ConfWeightItem> tmpMap = new HashMap<>();
        for (Conf x : ord) {
            ConfWeightItem it = new ConfWeightItem(x);
            tmpMap.put(it.getConf().getName(), it);
        }

        // собираем информацию
        int idx = 0;
        int weight;
        for (Conf x : src) {
            ConfWeightItem it = tmpMap.get(x.getName());
            if (it != null) {
                weight = it.getWeight();
            } else {
                weight = 50;
            }
            idx++;
            tmp.add(new ConfWeightItem(x, weight, idx));
        }

        // сортируем
        Collections.sort(tmp);

        // делаем результат
        for (ConfWeightItem it : tmp) {
            res.add(it.getConf());
        }

        return res;
    }

    /**
     * Синонм для sortByWeight(src,src)
     */
    public static List<Conf> sortByWeight(Collection<Conf> src) {
        return sortByWeight(src, src);
    }

    /**
     * Возвращает true, если conf имеет атрибут attrName и его значение true
     */
    public static boolean isTagged(Conf conf, String attrName) {
        return conf.getBoolean(attrName, false);
    }

    /**
     * Возвращает имя conf с учетом того, что это может быть путь.
     * Заменяет в имени '!' на '/' и нормализует путь.
     */
    public static String getNameAsPath(Conf x) {
        return UtVDir.normalize(x.getName().replace('!', '/'));
    }

    /**
     * Возвращает атрибут conf с учетом того, что это может быть путь.
     * Заменяет в имени '!' на '/' и нормализует путь.
     */
    public static String getAttrAsPath(Conf x, String attrName) {
        return UtVDir.normalize(x.getString(attrName).replace('!', '/'));
    }

}
