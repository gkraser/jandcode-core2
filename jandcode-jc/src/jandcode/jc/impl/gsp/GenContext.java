package jandcode.jc.impl.gsp;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;

/**
 * Контекст генерации
 */
public class GenContext {

    private String basedir;
    private String charset = UtString.UTF8;
    private StackList<GenFile> stack = new StackList<>();
    private IVariantMap args = new VariantMap();
    private boolean fakeMode;

    public GenContext(String basedir, String charset) {
        this.basedir = basedir;
        this.charset = charset;
    }

    public GenContext(String basedir, boolean fakeMode) {
        this.basedir = basedir;
        this.fakeMode = fakeMode;
    }

    public String getBasedir() {
        return basedir;
    }

    /**
     * true - решим игнорирования файловых операций и вывода
     */
    public boolean isFakeMode() {
        return fakeMode;
    }

    /**
     * Глобальные для всех файлов аргументы
     */
    public IVariantMap getArgs() {
        return args;
    }

    public void pushFile(String filename, boolean append) {
        String fn = UtFile.abs(UtFile.join(basedir, filename));
        GenFile f = new GenFile(this, fn, append, this.charset);
        stack.push(f);
    }

    public void popFile() {
        if (stack.size() <= 0) {
            throw new XError("popFile без pushFile (context)");
        }
        GenFile f1 = stack.pop();
        f1.close();
    }

    public void close() {
        for (GenFile f : stack) {
            f.close();
        }
        stack.clear();
    }

    public GenFile getCurrentFile() {
        return stack.last();
    }

    public int getStackSize() {
        return stack.size();
    }

}
