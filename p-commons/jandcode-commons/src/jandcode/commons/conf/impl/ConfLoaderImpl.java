package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.str.*;
import org.apache.commons.vfs2.*;

import java.io.*;
import java.text.*;
import java.util.*;

public class ConfLoaderImpl implements ConfLoaderContext {

    // стек файлов
    private StackList<String> stackFiles = new StackList<>();

    // использованные conf+file
    private Map<Conf, Set<String>> usedFiles = new HashMap<>();

    // загруженные файлы в порядке загрузки
    private Set<String> loadedFiles = new LinkedHashSet<>();

    // плагины
    private List<ConfLoaderPlugin> plugins = new ArrayList<>();

    // раскрытие переменных #{x}
    private SubstVarParser varExpander = new VarExpander();

    // перемнные
    private Map<String, String> vars = new LinkedHashMap<>();

    protected Conf root;

    class VarExpander extends SubstVarParser {

        VarExpander() {
            startVar1 = '#';
        }

        public String onSubstVar(String v) {
            try {
                String res = getVar(v);
                if (res == null) {
                    res = "";
                }
                return res;
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
    }

    //////

    public ConfLoaderImpl(Conf root) {
        this.root = root;
        registerPlugin(new SystemConfLoaderPlugin());
    }

    public Conf getRoot() {
        return root;
    }

    ////// ILoader

    public void loadFrom(Reader reader) throws Exception {

        String fn = UtLoad.getFilename(reader);
        if (UtString.empty(fn)) {
            fn = UtFile.join(UtFile.getWorkdir(), "dummy.cfx");
        }
        fn = UtFile.getFileObject(fn).toString();
        push(root, fn);
        try {
            //
            ILoader ldr = createFileLoader(root, fn);
            ldr.loadFrom(reader);

            //
            for (ConfLoaderPlugin plugin : plugins) {
                plugin.afterLoad();
            }

        } finally {
            //
            if (!UtString.empty(fn)) {
                pop();
            }
        }
    }

    static ServiceLoader<ConfFileLoaderFactory> fileLoaderFactorys;

    /**
     * Создать загрузчик указанного файла
     *
     * @param root для какой конфигурации
     * @param fn   имя файла полное. По нему определяется тип загрузчика
     */
    private ILoader createFileLoader(Conf root, String fn) {
        String ext = UtFile.ext(fn);
        if ("cfx".equals(ext)) {
            return new ConfFileLoader_cfx(root, this);
        }
        if ("json".equals(ext)) {
            return new ConfFileLoader_json(root, this);
        }
        if ("xml".equals(ext)) {
            return new ConfFileLoader_xml(root, this);
        }
        //
        if (fileLoaderFactorys == null) {
            synchronized (Conf.class) {
                if (fileLoaderFactorys == null) {
                    fileLoaderFactorys = ServiceLoader.load(ConfFileLoaderFactory.class);
                }
            }
        }
        for (ConfFileLoaderFactory factory : fileLoaderFactorys) {
            ILoader ldr = factory.createFileLoader(ext, root, this);
            if (ldr != null) {
                return ldr;
            }
        }
        throw new XError("Не найден загрузчик для типа файла [{0}]", ext);
    }

    public LoadFrom load() {
        return new LoadFrom(this);
    }

    //////

    public void registerPlugin(ConfLoaderPlugin pluginInst) {
        plugins.add(0, pluginInst);
        try {
            pluginInst.initPlugin(this);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public List<ConfLoaderPlugin> getPlugins() {
        return plugins;
    }

    public Collection<String> getLoadedFiles() {
        return loadedFiles;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public String getVar(String varName) {
        String s;
        try {
            for (ConfLoaderPlugin p : plugins) {
                s = p.getVar(varName);
                if (s != null) {
                    return s;
                }
            }
        } catch (Exception e) {
            throw new XErrorMark(e, "переменная " + varName);
        }
        return vars.get(varName);
    }

    public String expandVars(String s) {
        if (s == null) {
            return "";
        }
        if (s.indexOf('#') != -1) {  // ускоряем процесс
            varExpander.loadFrom(s);
            return varExpander.getResult();
        } else {
            return s;
        }
    }

    public void execFunc(String funcName, Conf params, Conf context) {
        for (ConfLoaderPlugin p : plugins) {
            try {
                if (p.execFunc(funcName, params, context)) {
                    return;
                }
            } catch (Exception e) {
                throw new XErrorMark(e, MessageFormat.format("функция [{0}]", funcName));
            }
        }
        throw new XError("Неизвестная функция [{0}]", funcName);
    }

    public Object evalExpression(Conf expr) {
        for (ConfLoaderPlugin p : plugins) {
            try {
                Object v = p.evalExpression(expr);
                if (v != null) {
                    return v;
                }
            } catch (Exception e) {
                throw new XErrorMark(e, MessageFormat.format("выражение [{0}]", expr));
            }
        }
        throw new XError("Неизвестное выражение [{0}]", expr);
    }

    ////// include

    public void includePath(Conf dest, String path, boolean required) throws Exception {
        path = getAbsPath(path);

        if (path.indexOf('*') == -1 && path.indexOf('?') == -1) {
            // one file
            FileObject fo = UtFile.getFileObject(path);
            if (!fo.exists()) {
                if (required) {
                    throw new XError(UtLang.t("Не найден файл [{0}]"), path);
                } else {
                    return;
                }
            }
            includeOneFile(dest, fo);
        } else {
            //
            DirScanner<FileObject> sc = UtFile.createDirScannerVfs(path);
            List<FileObject> lst = sc.load();
            if (required && lst.size() == 0) {
                throw new XError(UtLang.t("Не найдены файлы по маске [{0}]"), path);
            }
            for (FileObject f : lst) {
                includeOneFile(dest, f);
            }
        }

    }

    /**
     * Включение одного файла. Если файл уже загружался, то вызов игнорируется
     *
     * @param dest куда
     * @param f    откуда
     */
    private void includeOneFile(Conf dest, FileObject f) throws Exception {
        String fn = f.toString();
        if (isUsed(dest, fn)) {
            return;  // уже грузили этот файл в этот узел
        }
        push(dest, fn);
        try {
            ILoader ldr = createFileLoader(dest, fn);
            UtLoad.fromFileObject(ldr, f);
        } finally {
            pop();
        }
    }

    //////

    /**
     * В стек комбинацию dest+filename
     */
    private void push(Conf dest, String filename) {
        stackFiles.push(filename);
        Set<String> a = usedFiles.get(dest);
        if (a == null) {
            a = new HashSet<>();
            usedFiles.put(dest, a);
        }
        a.add(filename);
        loadedFiles.add(filename);
    }

    /**
     * Из стека
     */
    private void pop() {
        stackFiles.pop();
    }

    /**
     * Была ли использована комбинация dest+filename
     */
    private boolean isUsed(Conf dest, String filename) {
        Set<String> a = usedFiles.get(dest);
        return a != null && a.contains(filename);
    }

    //////

    public String getAbsPath(String path) {
        if (!UtFile.isAbsolute(path)) {
            String curPath;
            if (stackFiles.size() == 0) {
                curPath = UtFile.getWorkdir();
            } else {
                curPath = UtFile.path(stackFiles.last());
            }
            path = UtFile.join(curPath, path);
        }
        return UtFile.getFileObject(path).toString();
    }

    ////// origin

    public void addOrigin(Conf conf, String prop, int lineNum) {
        String fn = this.stackFiles.last();
        ConfOriginManager.getInst().addOrigin(conf, prop, fn, lineNum);
    }

}
