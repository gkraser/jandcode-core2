package jandcode.jc.impl.lib;

import jandcode.commons.*;
import jandcode.commons.moduledef.*;
import jandcode.jc.*;
import jandcode.jc.impl.depends.*;
import org.apache.commons.vfs2.*;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.regex.*;

/**
 * Утилиты для библиотек
 */
public class LibUtils {

    /**
     * Суффиксы для исходников
     */
    private static String[] SRC_SUFFIX = {"-src", "-source", "-sources"};

    /**
     * Из имени типа 'xxx-yyy-1.0' убирает версию и делает имя 'xxx-yyy'.
     * Из имени типа 'xxx-yyy-1.0-src' или 'xxx-yyy-src' убирает версию и '-src'
     * и делает имя 'xxx-yyy'.
     */
    public static String filenameToLibname(String filename) {
        String basename = UtFile.basename(filename);
        for (String sf : SRC_SUFFIX) {
            String s = UtString.removeSuffix(basename, sf);
            if (s != null) {
                basename = s;
                break;
            }
        }
        return removeVersionFromBasename(basename);
    }

    /**
     * Из имени типа 'xxx-yyy-1.0' убирает версию и делает имя 'xxx-yyy'
     */
    protected static String removeVersionFromBasename(String basename) {
        StringBuilder res = new StringBuilder();
        StringTokenizer tk = new StringTokenizer(basename, "-");
        while (tk.hasMoreTokens()) {
            String s = tk.nextToken();
            if (s.length() > 0) {
                char c = s.charAt(0);
                if (UtString.isNumChar(c)) {
                    // начало версии. досвиданья
                    break;
                }
            }
            if (res.length() != 0) {
                res.append('-');
            }
            res.append(s);
        }
        return res.toString();
    }

    /**
     * Версия из jar-файла
     */
    public static String getJarVersion(Manifest mn) {
        if (mn != null) {
            String v = internal_getJarVersion_Attrs(mn.getMainAttributes());
            if (v != null) {
                return internal_getJarVersion_Parse(v);
            }
            for (Map.Entry<String, Attributes> en : mn.getEntries().entrySet()) {
                v = internal_getJarVersion_Attrs(en.getValue());
                if (v != null) {
                    return internal_getJarVersion_Parse(v);
                }
            }
        }
        return null;
    }

    private static String internal_getJarVersion_Parse(String v) {
        v = v.trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (!(UtString.isIdnChar(c) || c == '.' || c == '-')) {
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static String internal_getJarVersion_Attrs(Attributes at) {
        String v;
        v = at.getValue(JcConsts.MANIFEST_LIB_VERSION);
        if (v != null) {
            return v;
        }
        v = at.getValue("Implementation-Version");
        if (v != null) {
            return v;
        }
        v = at.getValue("Specification-Version");
        if (v != null) {
            return v;
        }
        v = at.getValue("Bundle-Version");
        if (v != null) {
            return v;
        }
        v = at.getValue("Version");
        if (v != null) {
            return v;
        }
        return null;
    }

    /**
     * Зависимости библиотеки из jar-файла.
     *
     * @param ctx     Ctx
     * @param libName имя библиотеки. К этому имени привязывается результат для
     *                формирования сообщения об ошибках
     */
    public static LibDepends getJarLibDepends(Ctx ctx, String libName, Manifest mn) {
        LibDepends res = new LibDependsImpl(ctx, libName);
        if (mn == null) {
            return res;
        }
        Attributes attrs = mn.getMainAttributes();
        String pfx = JcConsts.MANIFEST_LIB_DEPENDS + "-";
        for (Object k : attrs.keySet()) {
            String key = k.toString();
            String depGroup = UtString.removePrefix(key, pfx);
            if (depGroup == null) {
                continue;
            }
            List<String> lst = UtCnv.toList(attrs.get(k));
            res.addGroup(depGroup).add(lst);
        }
        return res;
    }

    /**
     * Зависимости список модулей из jar-файла
     */
    public static List<String> getJarModules(Manifest mn) {
        List<String> res = new ArrayList<>();
        if (mn == null) {
            return res;
        }
        String s = mn.getMainAttributes().getValue(ModuleDefConsts.MANIFEST_MODULES);
        if (!UtString.empty(s)) {
            res.addAll(UtCnv.toList(s));
        }
        return res;
    }

    /**
     * Поиск jar файла, в котором определен указанный ресурс.
     *
     * @param resourcePath путь до ресурса (например 'jandcode/jc")
     * @return null, если не найден или находится не в jar
     */
    public static String findJarFileByResource(String resourcePath) {
        resourcePath = "res:" + UtVDir.normalize(resourcePath);
        FileObject f;
        try {
            f = UtFile.getFileObject(resourcePath);
        } catch (Exception e) {
            return null;
        }
        String s = f.toString();
        s = UtString.removePrefix(s, "jar:file://");
        if (s == null) {
            return null;
        }
        int a = s.indexOf("!");
        if (a == -1) {
            return null;
        }
        s = s.substring(0, a);
        s = new File(s).getAbsolutePath();
        return s;
    }


    /**
     * Поиск jar файла, в котором лежит указанный класс
     *
     * @param className имя класса
     * @return null, если не найден или находится не в jar
     */
    public static String findJarFileByClassname(String className) {
        String s = className.replace(".", "/") + ".class";
        return findJarFileByResource(s);
    }

    /**
     * Извлечь версию из строки
     *
     * @param s исходная строка, обычно имя файла
     * @return null, если версия не обнаружена
     */
    public static String extractVersion(String s) {
        Pattern p = Pattern.compile("\\d+(\\.\\d+)*");
        Matcher m = p.matcher(s);
        if (m.find()) {
            return m.group();
        }
        return null;
    }
}
