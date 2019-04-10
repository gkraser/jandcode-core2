package jandcode.commons.groovy.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.error.impl.*;
import jandcode.commons.groovy.*;
import jandcode.commons.io.*;
import org.apache.commons.vfs2.*;
import org.slf4j.*;

import java.io.*;

/**
 * Исходник скрипта
 */
public class GroovyClazzImpl implements GroovyClazz {

    protected static Logger log = LoggerFactory.getLogger(GroovyCompiler.class);

    private GroovyCompilerImpl compiler;
    private String sourceOriginal;
    private String sourceClazz;
    private FileObject sourceFile;
    private Class clazz;
    private String className;
    private int importCountLines;
    private int appendLines;        // сколько строк добавили к тексту
    private boolean template;       // шаблон ли?
    private long lastModTime = -1;
    private Class baseClass;
    private String sign;

    public GroovyClazzImpl(GroovyCompilerImpl compiler) {
        this.compiler = compiler;
    }

    public GroovyCompilerImpl getCompiler() {
        return compiler;
    }

    public String getSourceOriginal() {
        return sourceOriginal;
    }

    public String getSourceClazz() {
        return sourceClazz;
    }

    public FileObject getSourceFile() {
        return sourceFile;
    }

    public Class getClazz() {
        if (clazz == null) {
            // compile
            synchronized (this) {
                if (clazz != null) {
                    return clazz;
                }
                try {
                    if (sourceFile != null) {
                        StringLoader ldr = new StringLoader();
                        UtLoad.fromFileObject(ldr, sourceFile);
                        sourceOriginal = ldr.getResult();
                    }

                    String src = sourceOriginal;
                    if (template) {
                        GspParser p = new GspParser();
                        p.loadFrom(src);
                        src = p.getScriptText();
                    }
                    sourceClazz = makeClassText(src, className, baseClass, sign);
                    //

                    if (!isNeedRecompile()) {
                        try {
                            if (log.isInfoEnabled()) {
                                log.info("find class: " + className);
                            }
                            clazz = UtClass.classForName(className);
                            if (log.isInfoEnabled()) {
                                log.info("cashed class: " + className);
                            }
                        } catch (ClassNotFoundException e) {
                            if (log.isInfoEnabled()) {
                                log.info("class not found: " + className);
                            }
                        }
                    }

                    if (clazz == null) {
                        // не смогли или не захотели загружать
                        if (log.isInfoEnabled()) {
                            log.info("compile class: " + className);
                        }
                        clazz = compiler.getGcl().parseClass(sourceClazz, className);
                        if (sourceFile != null) {
                            lastModTime = sourceFile.getContent().getLastModifiedTime();
                        }
                    }
                } catch (Exception e) {
                    throw new XErrorWrap(e);
                }

            }
        }
        return clazz;
    }

    public Object createInst() {
        return UtClass.createInst(getClazz());
    }

    //////

    protected String makeClassText(String text, String className, Class baseClass, String sign) {
        StringBuilder sb = new StringBuilder();

        // препроцессор
        text = compiler.preprocessScriptText(text, className, baseClass, sign);

        if (GroovyCompiler.SIGN_CLASS.equals(sign)) {
            // полный класс
            sb.append(text);
        } else {
            GroovyScriptSplitter p = new GroovyScriptSplitter(text);

            importCountLines = p.getImportCountLines();
            sb.append(p.getImportPart());
            appendLines = 2;
            sb.append("\npublic class ").append(className).
                    append(" extends ").append(baseClass.getName()).
                    append(" {\n");
            if (GroovyCompiler.SIGN_BODY.equals(sign)) {
                sb.append(p.getBodyPart());
            } else {
                appendLines++; // на сигнатуру метода
                sb.append(sign).append(" {\n");
                sb.append(p.getBodyPart());
                sb.append("\n}");
            }
            sb.append("\n}");
        }

        return sb.toString();
    }

    public void loadText(String text, Class baseClass, String sign, boolean template, String className) throws Exception {
        this.clazz = null;
        this.sourceOriginal = text;
        this.baseClass = baseClass;
        this.sign = sign;
        this.template = template;
        this.className = className;
    }

    public void loadFile(FileObject file, Class baseClass, String sign, boolean template, String className) throws Exception {
        this.clazz = null;
        this.sourceFile = file;
        this.baseClass = baseClass;
        this.sign = sign;
        this.template = template;
        this.className = className;
    }

    //////

    /**
     * Возвращает {@link ErrorSource} для этого источника.
     *
     * @param line для какой строки (начиная с 1)
     */
    public ErrorSource createErrorSource(int line) {
        // line - это в sourceClass
        ErrorSourceImpl es = new ErrorSourceImpl();
        int lineOrig = line;
        if (line > importCountLines + appendLines) {
            lineOrig = lineOrig - appendLines;
        }
        es.setLineNum(lineOrig);
        es.setLineText(UtString.getLine(sourceOriginal, lineOrig - 1));
        if (template) {
            es.setLineTextPrepared(UtString.getLine(sourceClazz, line - 1));
        }
        if (sourceFile != null) {
            es.setSourceName(getSourceFile().toString());
        }
        return es;
    }

    //////

    /**
     * Проверка необходимости перекомпилирования
     *
     * @return
     */
    protected boolean isNeedRecompile() {
        if (!compiler.hasCompiledCacheDir()) {
            // нет каталога кеша, нужно
            return true;
        }
        if (sourceFile == null) {
            // нет связи с файлом - нужно
            return true;
        }
        if (lastModTime != -1) {
            // если не -1, то уже были попытки компиляции и кеш проверять не нужно
            return true;
        }
        File fc = new File(compiler.getCompiledCacheDir(), className + ".class");
        if (fc.exists()) {
            long tmCache = fc.lastModified();
            long tmSrc;
            try {
                tmSrc = sourceFile.getContent().getLastModifiedTime();
            } catch (Exception e) {
                return true;
            }
            if (tmSrc <= tmCache) {
                // время исходника не изменилось по сравнению с class
                return false;
            }
        }
        return true;
    }

    /**
     * Проверка, что изменился источник - файл
     */
    public boolean checkModify() {
        if (sourceFile == null) {
            // нет файла - бессмысленно
            return false;
        }
        if (clazz == null) {
            // не нужно проверять, еще класс не получали
            return false;
        }
        boolean res = false;
        long tm = 0;
        try {
            tm = sourceFile.getContent().getLastModifiedTime();
            if (tm != lastModTime) {
                // изменился - сбрасываем класс
                clazz = null;
                res = true;
                if (log.isInfoEnabled()) {
                    log.info("found change in: " + sourceFile.toString());
                }
            }
        } catch (FileSystemException e) {
            // при ошибке - файл не существует, то же сбрасываем класс
            clazz = null;
        }
        return res;
    }

}
