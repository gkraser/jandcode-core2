package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.groovy.*;
import jandcode.commons.source.*;
import jandcode.jc.*;
import org.apache.commons.vfs2.*;

import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

/**
 * Скрипты.
 */
@SuppressWarnings("unchecked")
public class ScriptHolder implements IScripts {

    private static Pattern P_BEFORELOAD = Pattern.compile("static\\s+beforeLoad\\s*=\\s*(\\{.*)", Pattern.DOTALL);

    private Ctx ctx;
    private GroovyCompiler compiler;
    private Map<Class, String> scriptFilesByClass = new HashMap<>();
    private Map<String, String> beforeLoadFilesCache = new HashMap<>();

    public ScriptHolder(Ctx ctx) {
        this.ctx = ctx;
    }

    public GroovyCompiler getCompiler() {
        if (compiler == null) {
            synchronized (this) {
                if (compiler == null) {
                    compiler = UtGroovy.createCompiler();

                    // cache
                    if (!ctx.getConfig().isRunAsProduct()) {
                        String cacheDir = ctx.getTempdir("classes");
                        ctx.debug(MessageFormat.format("classes cache dir set to [{0}]", cacheDir));
                        compiler.setCompiledCacheDir(cacheDir);
                    }
                }
            }
        }
        return compiler;
    }

    //////

    public Class getClassProjectScript(String scriptName, Project project) {
        Class cls;
        if (scriptName.indexOf('/') == -1 || scriptName.indexOf('\\') != -1) {
            try {
                cls = Class.forName(scriptName, true, Thread.currentThread().getContextClassLoader());
                // класс найден - это класс
                return cls;
            } catch (Exception e) {
                // ignore
            }
        }
        // ну тогда файл

        if (!UtFile.isAbsolute(scriptName)) {
            // не абсолютное - ищем относительно проекта
            if (project != null) {
                scriptName = project.wd(scriptName);
            }
        }

        FileObject f = UtFile.getFileObject(scriptName);

        String ext = f.getName().getExtension();
        boolean isGsp = "gsp".equals(ext);

        // возвращаем класс
        Class baseClass = ProjectScriptImpl.class;
        if (isGsp) {
            baseClass = GspScript.class;
        }
        GroovyClazz gc = null;
        try {
            if (isGsp) {
                gc = getCompiler().getClazz(baseClass, "void onGenerate()", f, true);
            } else {
                gc = getCompiler().getClazz(baseClass, GroovyCompiler.SIGN_BODY, f, false);
            }
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }

        Class res = gc.getClazz();
        if (!isGsp) {
            // в файле может быть описан полный класс. Ищем его
            int cnt = 0;

            for (Class aClass : res.getDeclaredClasses()) {
                if (ProjectScriptImpl.class.isAssignableFrom(aClass)) {
                    // имеется внутренний класс ProjectScript. Возвращаем его
                    res = aClass;
                    cnt++;
                    if (cnt > 1) {
                        throw new XError("В файле {0} объявлено более одного класса ProjectScript");
                    }
                }
            }
        }

        // кешируем связку имени файла и класса
        scriptFilesByClass.put(res, scriptName);

        return res;
    }

    public IProjectScript createProjectScript(Class cls) {
        Class encls = cls.getEnclosingClass();
        if (encls != null && !Modifier.isStatic(cls.getModifiers())) {
            // это локальный класс. Создаем через публичный
            Object top = UtClass.createInst(encls);
            try {
                Constructor cnst = cls.getConstructor(encls);
                return (IProjectScript) cnst.newInstance(top);
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        } else {
            // это обычный публичный класс
            return (IProjectScript) UtClass.createInst(cls);
        }
    }

    public String getFileProjectScript(Class cls) {
        return scriptFilesByClass.get(cls);
    }

    public String getBeforeLoadProjectScript(String filename) {
        filename = UtFile.absCanonical(filename);
        if (beforeLoadFilesCache.containsKey(filename)) {
            return beforeLoadFilesCache.get(filename);
        }

        //
        String res = null;
        try {
            String text = UtFile.loadString(new File(filename));
            Matcher m = P_BEFORELOAD.matcher(text);
            if (m.find()) {

                String impText = "import jandcode.jc.*\n" +
                        "import jandcode.jc.std.*\n" +
                        "import jandcode.commons.*\n";

                // текст closure и все, что после нее
                text = m.group(1);

                JavaSimpleBlockExtractor ex = new JavaSimpleBlockExtractor();
                ex.load().fromString(text);
                text = ex.getResult();
                if (!UtString.empty(text.trim())) {
                    // имеем текст closure

                    text = UtString.normalizeIndent(text).trim();

                    String scdir = ctx.getTempdir("beforeLoadScripts");

                    // формируем имя файла
                    String gfn = scdir + "/beforeLoad_" + UtString.md5Str(impText + "\n" + text) + ".jc";
                    if (!UtFile.exists(gfn)) {

                        // создаем каталог для скриптов
                        if (!UtFile.exists(scdir)) {
                            UtFile.mkdirs(scdir);
                        }

                        // создаем текст класса

                        StringBuilder sb = new StringBuilder();
                        sb.append(impText);
                        sb.append("\n");
                        sb.append("class BeforeLoad_P extends ProjectScript implements IBeforeLoadScript {\n");
                        sb.append("void executeBeforeLoad(){\n");
                        sb.append(text);
                        sb.append("\n");
                        sb.append("}\n}\n");

                        // пишем его в кеш
                        UtFile.saveString(sb.toString(), new File(gfn));

                    }
                    //
                    res = gfn;

                    ctx.debug(MessageFormat.format("beforeLoad скрипт для [{0}] в файле [{1}]", filename, res));

                }

            }

            beforeLoadFilesCache.put(filename, res);
            return res;
        } catch (Exception e) {
            throw new XErrorMark(e, "beforeLoad для файла: " + filename);
        }

    }
}
