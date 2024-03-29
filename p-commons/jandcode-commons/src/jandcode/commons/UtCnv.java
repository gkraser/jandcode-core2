package jandcode.commons;

import jandcode.commons.datetime.*;
import jandcode.commons.error.*;
import jandcode.commons.impl.variant.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.awt.*;
import java.math.*;
import java.time.*;
import java.util.List;
import java.util.*;

/**
 * Утилиты для стандартной конвертации значений
 */
@SuppressWarnings("unchecked")
public class UtCnv {

    protected static byte[] EMPTY_BYTEARRAY = new byte[0];

    /**
     * Символы для конвертации по основанию
     */
    protected static final char[] radixChars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '_', '-',
    };

    //////

    /**
     * Конвертация в integer.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static int toInt(Object value, int defValue) {
        try {
            if (value == null) {
                return defValue;
            } else if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof Long) {
                long v = (Long) value;
                if (v > Integer.MAX_VALUE || v < Integer.MIN_VALUE) {
                    return defValue;
                } else {
                    return (int) v;
                }
            } else if (value instanceof Double) {
                double v = (Double) value;
                if (v > Integer.MAX_VALUE || v < Integer.MIN_VALUE) {
                    return defValue;
                } else {
                    return (int) v;
                }
            } else if (value instanceof Number) {
                return ((Number) value).intValue();
            } else if (value instanceof String) {
                if (((String) value).length() == 0) {
                    return defValue;
                } else {
                    Double n = Double.parseDouble((String) value);
                    return n.intValue();
                }
            } else if (value instanceof Boolean) {
                if ((Boolean) value) {
                    return 1;
                } else {
                    return 0;
                }
            } else
                return defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Конвертация в integer.
     */
    public static int toInt(Object value) {
        return toInt(value, 0);
    }

    /**
     * Конвертация в long.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static long toLong(Object value, long defValue) {
        try {
            if (value == null) {
                return defValue;
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            } else if (value instanceof String) {
                if (((String) value).length() == 0) {
                    return defValue;
                } else {
                    Double n = Double.parseDouble((String) value);
                    return n.longValue();
                }
            } else if (value instanceof Boolean) {
                if ((Boolean) value) {
                    return 1L;
                } else {
                    return 0L;
                }
            } else
                return defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Конвертация в long.
     */
    public static long toLong(Object value) {
        return toLong(value, 0L);
    }

    /**
     * Конвертация в double.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static double toDouble(Object value, double defValue) {
        try {
            if (value == null) {
                return defValue;
            } else if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else if (value instanceof String) {
                if (((String) value).length() == 0) {
                    return defValue;
                } else {
                    return Double.parseDouble((String) value);
                }
            } else if (value instanceof Boolean) {
                if ((Boolean) value) {
                    return 1.0;
                } else {
                    return 0.0;
                }
            } else
                return defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Конвертация в double.
     */
    public static double toDouble(Object value) {
        return toDouble(value, 0.0);
    }

    /**
     * Конвертация в String.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static String toString(Object value, String defValue) {
        try {
            if (value == null) {
                return defValue;
            } else if (value instanceof String) {
                return (String) value;
            } else if (value instanceof Boolean) {
                if ((Boolean) value) {
                    return "true"; //NON-NLS
                } else {
                    return "false"; //NON-NLS
                }
            } else if (value instanceof XDateTime) {
                return value.toString();
            } else if (value instanceof XDate) {
                return value.toString();
            } else if (value instanceof byte[]) {
                return UtString.encodeBase64((byte[]) value);
            } else
                return value.toString();
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Конвертация в String.
     */
    public static String toString(Object value) {
        return toString(value, "");
    }

    /**
     * Конвертация в boolean.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static boolean toBoolean(Object value, boolean defValue) {
        try {
            if (value == null) {
                return defValue;
            } else if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof Number) {
                return ((Number) value).doubleValue() == 1.0;
            } else if (value instanceof CharSequence) {
                String s = value.toString();
                if (s.equalsIgnoreCase("true")) { //NON-NLS
                    return true;
                } else if (s.equalsIgnoreCase("on")) { //NON-NLS
                    return true;
                } else if (s.equals("1")) {
                    return true;
                } else if (s.equals("t")) {
                    return true;
                }
                if (defValue && s.length() > 0) {
                    if (s.equalsIgnoreCase("false")) { //NON-NLS
                        return false;
                    } else if (s.equalsIgnoreCase("off")) { //NON-NLS
                        return false;
                    } else if (s.equals("0")) {
                        return false;
                    } else if (s.equals("f")) {
                        return false;
                    }
                }
                return defValue;
            } else
                return defValue;
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Конвертация в boolean.
     */
    public static boolean toBoolean(Object value) {
        return toBoolean(value, false);
    }

    /**
     * Конвертация в XDateTime.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static XDateTime toDateTime(Object value, XDateTime defValue) {
        try {
            if (value == null) {
                return defValue;
            } else if (value instanceof String) {
                return XDateTime.create((String) value);
            } else if (value instanceof XDateTime) {
                return (XDateTime) value;
            } else if (value instanceof XDate) {
                return ((XDate) value).toDateTime();
            } else if (value instanceof java.util.Date) {
                return XDateTime.create((java.util.Date) value);
            } else if (value instanceof LocalDateTime) {
                return XDateTime.create(((LocalDateTime) value));
            } else if (value instanceof LocalDate) {
                return XDateTime.create(((LocalDate) value));
            } else if (value instanceof Number) {
                return XDateTime.create(((Number) value).longValue());
            } else {
                return defValue;
            }
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Конвертация в XDateTime.
     */
    public static XDateTime toDateTime(Object value) {
        return toDateTime(value, UtDateTime.EMPTY_DATETIME);
    }

    /**
     * Конвертация в XDate.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static XDate toDate(Object value, XDate defValue) {
        try {
            if (value == null) {
                return defValue;
            } else if (value instanceof String) {
                return XDate.create((String) value);
            } else if (value instanceof XDate) {
                return (XDate) value;
            } else if (value instanceof XDateTime) {
                return ((XDateTime) value).toDate();
            } else if (value instanceof java.util.Date) {
                return XDate.create((java.util.Date) value);
            } else if (value instanceof LocalDateTime) {
                return XDate.create(((LocalDateTime) value));
            } else if (value instanceof LocalDate) {
                return XDate.create(((LocalDate) value));
            } else if (value instanceof Number) {
                return XDate.create(((Number) value).longValue());
            } else {
                return defValue;
            }
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Конвертация в XDateTime.
     */
    public static XDate toDate(Object value) {
        return toDate(value, UtDateTime.EMPTY_DATE);
    }

    /**
     * Конвертация в byte array.
     * Если значение не может быть сконвертировано, возвращает значение по умолчанию defValue.
     */
    public static byte[] toByteArray(Object value, byte[] defValue) {
        try {
            if (value != null) {
                if (value instanceof String) {
                    return ((String) value).getBytes();
                } else if (value instanceof byte[]) {
                    return ((byte[]) value).clone();
                }
            }
        } catch (Exception e) {
            //
        }
        return defValue;
    }

    /**
     * Конвертация в blob
     */
    public static byte[] toByteArray(Object value) {
        return toByteArray(value, EMPTY_BYTEARRAY);
    }

    ////// swing-related

    /**
     * Строка вида 'WIDTH,HEIGHT' в Dimension
     */
    public static Dimension toDimension(String s) {
        Dimension d = new Dimension();
        if (UtString.empty(s)) {
            return d;
        }
        int a = s.indexOf(',');
        if (a == -1) {
            d.width = toInt(s.trim());
        } else {
            d.width = toInt(s.substring(0, a).trim());
            d.height = toInt(s.substring(a + 1).trim());
        }
        return d;
    }

    /**
     * Строка вида 'X,Y' в Point
     */
    public static Point toPoint(String s) {
        Dimension d = toDimension(s);
        return new Point(d.width, d.height);
    }

    /**
     * Строка вида 'T,L,B,R' в Insets
     */
    public static Insets toInsets(String s) {
        Insets d = new Insets(0, 0, 0, 0);
        if (UtString.empty(s)) {
            return d;
        }
        int a = s.indexOf(',');
        if (a != -1) {
            d.top = toInt(s.substring(0, a).trim());
            int b = s.indexOf(',', a + 1);
            if (b != -1) {
                d.left = toInt(s.substring(a + 1, b).trim());
                a = s.indexOf(',', b + 1);
                if (a != -1) {
                    d.bottom = toInt(s.substring(b + 1, a).trim());
                    d.right = toInt(s.substring(a + 1).trim());
                }
            } else {
                d.left = toInt(s.substring(a + 1).trim());
                d.right = d.left;
                d.bottom = d.top;
            }
        } else {
            d.top = toInt(s.trim());
            d.bottom = d.top;
        }
        return d;
    }

    /**
     * Строка вида 'key:value;key:value' в Map. Пробелы в key и value обрезаются.
     *
     * @param dest       куда писать результат
     * @param s          исходная строка
     * @param itemDelim  разделитель элементов (в примере выше это ';')
     * @param valueDelim разделитель значения (в примере выше это ':')
     */
    public static void toMap(Map dest, String s, String itemDelim, String valueDelim) {
        if (UtString.empty(s)) {
            return;
        }
        String ar[] = s.split(itemDelim);
        for (String s1 : ar) {
            int a = s1.indexOf(valueDelim);
            if (a == -1) {
                dest.put(s1.trim(), "");
            } else {
                dest.put(s1.substring(0, a).trim(), s1.substring(a + 1).trim());
            }
        }
    }

    /**
     * Строка вида 'key:value;key:value' в Map
     */
    public static VariantMapNoCase toMap(String s) {
        VariantMapNoCase res = new VariantMapNoCase();
        toMap(res, s, ";", ":");
        return res;
    }

    /**
     * Строка с разделителями или коллекция в список строк. Если элемент
     * коллекции INamed, то в результат кладется его имя.
     *
     * @param v          строка или коллекция
     * @param delimiters строка с символами-разделителями
     */
    public static List<String> toList(Object v, String delimiters) {
        List<String> res = new ArrayList<String>();
        if (v == null) {
            return res;
        }
        if (v instanceof CharSequence) {
            String s = v.toString();
            if (UtString.empty(s)) {
                return res;
            }
            StringTokenizer tk = new StringTokenizer(s, delimiters);
            while (tk.hasMoreTokens()) {
                String s1 = tk.nextToken().trim();
                if (s1.length() > 0) {
                    res.add(s1);
                }
            }
        } else if (v instanceof Collection) {
            Collection x = (Collection) v;
            for (Object z : x) {
                if (z instanceof INamed) {
                    res.add(((INamed) z).getName());
                } else {
                    res.add(UtString.toString(z));
                }
            }
        }
        return res;
    }

    /**
     * Строка с разделителями в List. В качестве разделителей используются пробельные
     * символы, запятая, точка с запятой.
     */
    public static List<String> toList(Object v) {
        return toList(v, " \t\n\r,;");
    }

    public static class ComplexValue extends Named {
        private String _name2;
        private String _value;

        public ComplexValue(String name, String value) {
            int a = name.indexOf('.');
            if (a == -1) {
                this.name = name;
                _name2 = "";
            } else {
                _name2 = name.substring(a + 1);
                this.name = name.substring(0, a);
            }
            _value = value;
        }

        public boolean hasName2() {
            return _name2 != null && _name2.length() > 0;
        }

        public String getName2() {
            return _name2;
        }

        public String getValue() {
            return _value;
        }

        public int getValueInt() {
            return toInt(_value);
        }

        public Point getValuePoint() {
            return toPoint(_value);
        }
    }

    /**
     * Строка вида 'name[.name2]:value;name[.name2]:value' в список ComplexValue
     */
    public static List<ComplexValue> toComplexList(String s) {
        VariantMap m = toMap(s);
        List<ComplexValue> res = new ArrayList<ComplexValue>();
        for (String key : m.keySet()) {
            ComplexValue x = new ComplexValue(key, m.getString(key));
            res.add(x);
        }
        return res;
    }

    /**
     * Переводит массив в map по правилам:
     * - нечетные элементы - ключи, четные - значения
     * - если нечетный элемент - Map, то он забирается в результат и счетчик нечетности
     * сбрасывается.
     * <p>
     * Пример:
     * <pre>
     * UtCnv.toMap("param1", value1, "param2", value2)
     * UtCnv.toMap(map1, "param1", value1, map2, map3)
     * </pre>
     */
    public static Map<String, Object> toMap(Object... arr) {
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        if (arr == null) {
            return m;
        }
        if (arr.length == 0) {
            return m;
        }
        int i = 0;
        while (i < arr.length) {
            Object it = arr[i];
            if (it instanceof Map) {
                m.putAll((Map) it);
                i++;
            } else {
                String k = it.toString();
                i++;
                Object v = arr[i];
                i++;
                m.put(k, v);
            }
        }
        return m;
    }

    ////// vfs

    /**
     * Класс в каталог в формате vfs
     */
    public static String toVfsPath(Class cls) {
        return "res:" + cls.getPackage().getName().replace('.', '/'); //NON-NLS
    }

    /**
     * Путь в путь в формате vfs. Если начинается с "/" - считается ресурсом
     */
    public static String toVfsPath(String s) {
        if (s.startsWith("/")) {
            return "res:" + s.substring(1); //NON-NLS
        }
        return s;
    }

    /**
     * Класс в файл в формате vfs
     *
     * @param ext расширение (с '.' в начале)
     */
    public static String toVfsFile(Class cls, String ext) {
        return "res:" + cls.getPackage().getName().replace(".", "/") + "/" + cls.getSimpleName() + ext; //NON-NLS
    }

    //////

    /**
     * Проверка на пустое значение (0, пустая строка...)
     */
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof Integer) {
            return ((Integer) value) == 0;
        } else if (value instanceof Long) {
            return ((Long) value) == 0;
        } else if (value instanceof Double) {
            return ((Double) value) == 0.0;
        } else if (value instanceof String) {
            return ((String) value).length() == 0;
        } else if (value instanceof XDateTime) {
            return UtDateTime.isEmpty((XDateTime) value);
        } else if (value instanceof XDate) {
            return UtDateTime.isEmpty((XDate) value);
        } else if (value instanceof byte[]) {
            return ((byte[]) value).length == 0;
        }
        return false;
    }

    /**
     * Преобразовать объект в IVariantNamed.
     *
     * @param value исходный объект. Может иметь тип:
     *              {@link IVariantNamed}, {@link IValueNamed}, Map,
     *              [long,int,string] (по имени 'id' возвращает значение, для остальных имен - null).
     *              null (для любого имени возвращает null)
     * @return враппер объекта.
     */
    public static IVariantNamed toVariantNamed(Object value) {
        if (value instanceof IVariantNamed) {
            return (IVariantNamed) value;
        } else if (value instanceof IValueNamed) {
            return new ValueNamedVariantNamed(value);
        } else if (value instanceof Map) {
            return new VariantMapWrap((Map) value);
        } else if (value instanceof Long || value instanceof Integer) {
            return new IdVariantNamed(value);
        } else if (value instanceof CharSequence) {
            return new IdVariantNamed(value.toString());
        } else {
            return new NullVariantNamed();
        }

    }

    //////

    /**
     * Объединение нескольких map в одну. Каждый элемент может быть null.
     * Позже встретившиеся ключи перезаписывают ранее встретившиеся. Объединяемые
     * map могут быть null.
     */
    public static Map joinMap(Map... m) {
        Map res = new HashMap<>();
        for (int i = 0; i < m.length; i++) {
            Map map = m[i];
            if (map != null) {
                res.putAll(map);
            }
        }
        return res;
    }

    //////

    /**
     * Для объекта возвращает его имя.
     * Если объект {@link INamed}, то возвращается его имя, иначе - строковое
     * представление объекта.
     */
    public static String toName(Object name) {
        if (name == null) {
            return "";

        } else if (name instanceof CharSequence) {
            return name.toString();

        } else if (name instanceof INamed) {
            return ((INamed) name).getName();

        } else {
            return name.toString();
        }
    }

    /**
     * Формирует список имен из произвольного объекта.
     */
    public static List<String> toNameList(Object names) {
        List<String> res = new ArrayList<>();
        if (names instanceof Collection) {
            for (Object it : ((Collection) names)) {
                String s = toName(it);
                if (!UtString.empty(s)) {
                    res.add(s);
                }
            }
        } else if (names instanceof CharSequence) {
            return toNameList(toList(names));
        } else {
            String s = toName(names);
            if (!UtString.empty(s)) {
                res.add(s);
            }
        }
        return res;
    }

    //////

    /**
     * Конвертация числа в строку по основанию.
     * Полученная строка регистрозависимая!
     * <p>
     * В основном функция используется с основанием 64
     * для уменьшения длины строки в 1.5 раза по сравнению с hex-преобразованием.
     *
     * @param i       исходное число
     * @param radix   основание (2..64)
     * @param minSize минимальный размер полученной строки. Если результат будет меньше
     *                этого размера, то он будет дополнен 0 слева до размера.
     *                При значении <0 игнорируется.
     * @return представление числа в виде строки с указанным основанием
     */
    public static String toRadix(long i, int radix, int minSize) {
        if (radix < 2 || radix > radixChars.length) {
            throw new XError("Invalid radix value");
        }
        if (minSize > radixChars.length) {
            minSize = radixChars.length;
        }
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);

        if (!negative) {
            i = -i;
        }

        while (i <= -radix) {
            buf[charPos--] = radixChars[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = radixChars[(int) (-i)];

        if (minSize > 0) {
            int sPos = 65 - minSize;
            while (charPos > sPos) {
                buf[--charPos] = radixChars[0];
            }
        }

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (65 - charPos));
    }

    //////

    /**
     * Округление double
     *
     * @param d     что округляем
     * @param scale сколько знаков оставляем после запятой. Если scale отрицательная,
     *              то округляем целую часть до указанного количества знаков.
     *              Например {@code round(123,-2)=>100}
     * @return округленное число
     */
    public static double round(double d, int scale) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return d;
        }
        if (scale >= 0) {
            BigDecimal decimal = new BigDecimal(d);
            decimal = decimal.setScale(scale, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        } else {
            double mn = Math.pow(10, -scale);
            return Math.round(d / mn) * mn;
        }
    }

}
