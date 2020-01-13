package jandcode.commons.groovy.impl;

import groovy.lang.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.groovy.*;
import org.apache.commons.vfs2.*;
import org.codehaus.groovy.control.*;
import org.slf4j.*;

import java.util.*;

public class GroovyCompilerImpl implements GroovyCompiler {

    protected static Logger log = LoggerFactory.getLogger(GroovyCompiler.class);

    public static final String CLASS_NAME_PREFIX = "G__";
    public static final String CLASS_NAME_DELIM = "__";

    private String compiledCacheDir;
    private GroovyClassLoader gcl;

    private HashMap<String, GroovyClazzImpl> cacheBySource = new HashMap<String, GroovyClazzImpl>();
    private HashMap<String, GroovyClazzImpl> cacheByClassName = new HashMap<String, GroovyClazzImpl>();
    private LinkedHashMap<String, GroovyPreprocessor> preprocessors;


    //////

    public GroovyClassLoader getGcl() {
        if (gcl == null) {
            synchronized (this) {
                if (gcl == null) {
                    CompilerConfiguration cc = new CompilerConfiguration();
                    if (compiledCacheDir != null) {
                        cc.setTargetDirectory(compiledCacheDir);
                    }
                    gcl = new GroovyClassLoader(UtClass.getClassLoader(), cc);
                }
            }
        }
        return gcl;
        //
    }

    public void destory() {
        gcl = null;
        cacheBySource.clear();
        cacheByClassName.clear();
        preprocessors = null;
        compiledCacheDir = null;
    }

    //////

    public void setCompiledCacheDir(String compiledCacheDir) {
        this.compiledCacheDir = compiledCacheDir;
        if (!UtString.empty(compiledCacheDir)) {
            UtClass.addClasspath(this.compiledCacheDir);
        }
        if (log.isInfoEnabled()) {
            log.info("script cached dir: " + this.compiledCacheDir);
        }
        // class loader нужно будет пересоздать
        gcl = null;
    }

    public String getCompiledCacheDir() {
        return compiledCacheDir;
    }

    public boolean hasCompiledCacheDir() {
        return compiledCacheDir != null;
    }

    //////

    public GroovyClazz getClazz(Class baseClass, String sign,
            FileObject file, boolean isTemplate) throws Exception {
        String fn = file.toString();
        String key = fn + "#" + baseClass.getName() + "#" + sign + "#" + isTemplate;
        GroovyClazzImpl res = cacheBySource.get(key);
        if (res != null) {
            return res;
        }
        //
        synchronized (this) {
            res = cacheBySource.get(key);
            if (res != null) {
                return res;
            }
            //
            res = new GroovyClazzImpl(this);
            String cn = CLASS_NAME_PREFIX + fileobjectToClassname(fn) + CLASS_NAME_DELIM + crcStr(key);
            cacheBySource.put(key, res);
            cacheByClassName.put(cn, res);
            res.loadFile(file, baseClass, sign, isTemplate, cn);
        }
        //
        res.getClazz(); // загружаем
        //
        return res;
    }

    public GroovyClazz getClazz(Class baseClass, String sign,
            String text, boolean isTemplate) throws Exception {
        String key = text + "#" + baseClass.getName() + "#" + sign + "#" + isTemplate;
        GroovyClazzImpl res = cacheBySource.get(key);
        if (res != null) {
            return res;
        }
        //
        synchronized (this) {
            res = cacheBySource.get(key);
            if (res != null) {
                return res;
            }
            //
            res = new GroovyClazzImpl(this);
            String cn = CLASS_NAME_PREFIX + crcStr(key);
            cacheBySource.put(key, res);
            cacheByClassName.put(cn, res);
            res.loadText(text, baseClass, sign, isTemplate, cn);
        }
        //
        res.getClazz(); // загружаем
        //
        return res;
    }

    public GroovyClazz findClazz(Class cls) {
        return cacheByClassName.get(cls.getName());
    }

    //////

    /**
     * Возвращает {@link ErrorSource} для указанного элемента стека.
     *
     * @param st для какого элемента стека
     * @return Если StackTraceElement не связан с исходником скриптов, возвращается null.
     */
    public ErrorSource getErrorSource(StackTraceElement st) {
        String cn = st.getClassName();
        if (UtString.empty(cn)) {
            return null;
        }
        int a = cn.indexOf('$');
        if (a != -1) {
            cn = cn.substring(0, a);
        }
        for (String ss : cacheByClassName.keySet()) {
            if (cn.equals(ss)) {
                return cacheByClassName.get(ss).createErrorSource(st.getLineNumber());
            }
        }
        return null;
    }

    /**
     * Возвращает {@link ErrorSource} для указанной строки
     * describer, где возможно есть упоминание имени класса.
     * Используется при поиске ошибок компиляции.
     *
     * @param describer строка, которая содержит имя класса
     * @param line      для какой строки
     * @return Если не найдено, возвращается null
     */
    public ErrorSource getErrorSource(String describer, int line) {
        GroovyClazzImpl ss = findGroovyClazzContaintsInStr(describer);
        if (ss != null) {
            return ss.createErrorSource(line);
        }
        return null;
    }

    /**
     * Возвращает GroovyClazz, который упомянут в строке
     *
     * @param s строка, например сообщение об ошибке, которая включает имя класса
     * @return null если не найдено умоминаний о скрипте
     */
    protected GroovyClazzImpl findGroovyClazzContaintsInStr(String s) {
        if (UtString.empty(s)) {
            return null;
        }
        for (String cn : cacheByClassName.keySet()) {
            if (s.indexOf(cn) != -1) {
                return cacheByClassName.get(cn);
            }
        }
        return null;
    }

    //////

    /**
     * Проверяем изменившиеся файлы
     */
    public boolean checkChangedResource() {
        boolean res = true;
        for (GroovyClazzImpl g : cacheBySource.values()) {
            if (g.checkModify()) {
                res = true;
            }
        }
        return res;
    }

    public void addPreprocessor(String name, GroovyPreprocessor preprocessor) {
        if (preprocessors == null) {
            preprocessors = new LinkedHashMap<String, GroovyPreprocessor>();
        }
        preprocessors.put(name, preprocessor);
    }


    public String preprocessScriptText(String text, String className, Class baseClass, String sign) {
        if (preprocessors == null) {
            return text;
        }
        for (GroovyPreprocessor p : preprocessors.values()) {
            text = p.preprocessScriptText(text, className, baseClass, sign);
        }
        return text;
    }

    private String fileobjectToClassname(String fileobjectName) {
        String[] ar = fileobjectName.split("/");
        String part1 = UtFile.removeExt(ar[ar.length - 1]);
        String part2 = "";
        String part3 = "";
        if (ar.length > 1) {
            part2 = ar[ar.length - 2];
        }
        if (ar.length > 2) {
            part3 = ar[ar.length - 3];
        }
        if (UtString.empty(part1)) {
            part1 = ar[ar.length - 1];
        }
        return normalizeClassName(part1 + CLASS_NAME_DELIM + part2 + CLASS_NAME_DELIM + part3);
    }

    private String normalizeClassName(String s) {
        if (UtString.empty(s)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (UtString.isLatChar(c) || UtString.isNumChar(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    private String crcStr(String key) {
        return UtString.md5Str(key).toLowerCase();
    }

}
