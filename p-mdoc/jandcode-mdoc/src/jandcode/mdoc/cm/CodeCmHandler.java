package jandcode.mdoc.cm;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.cm.snippet.*;
import jandcode.mdoc.groovy.*;
import jandcode.mdoc.gsp.*;
import jandcode.mdoc.source.*;

import java.util.*;

/**
 * Кусок кода для вставки в документ.
 */
public class CodeCmHandler extends BaseCmHandler {

    private Map<String, Snippet> cache = new LinkedHashMap<>();

    public String handleCm(IVariantMap attrs, OutFile outFile) throws Exception {
        boolean debug = getOutBuilder().getMode().isServeDebug();
        SourceFile f = getSourceFile(attrs, outFile);
        outFile.addDependFile(f); // в список зависимых, от snippet зависит текст
        Snippet snippet = getSnippet(f);
        String partName = attrs.getString("part");
        SnippetPart part = snippet.getPart(partName);
        if (part == null) {
            throw new XError("Not found part [{0}] ", partName);
        }

        // язык из атрибутов приоритетней
        String lang = attrs.getString("lang");
        if (UtString.empty(lang)) {
            lang = part.getLang();
        }

        String title = attrs.getString("title");

        //
        String s = "";
        s = s + "<div class=\"codeblock\" data-language=\"" + lang + "\">";
        if (!UtString.empty(title)) {
            s = s + "<div class=\"codeblock--title\">" + title + "</div>";
        }
        if (debug) {
            GspTemplateContext gctx = getOutBuilder().createGspTemplateContext(outFile);
            Map<String, Object> tmpAttrs = new LinkedHashMap<>(attrs);
            tmpAttrs.remove("$list");
            gctx.getArgs().put("attrs", tmpAttrs);
            gctx.getArgs().put("sourceFile", f);
            s = s + gctx.generate("_theme/debug/cm-code.gsp");
        }
        s = s + "<div class=\"codeblock--content\">";
        s = s + "<pre>";


        s = s + "<code class=\"language-" + lang + "\">" +
                UtString.xmlEscape(part.getText()) +
                "</code></pre>";
        s = s + "</div>";
        s = s + "</div>";

        return s;
    }

    private SourceFile getSourceFile(IVariantMap attrs, OutFile outFile) throws Exception {
        String file = attrs.getString("file");
        if (UtString.empty(file)) {
            throw new XError("Атрибут file не указан");
        }

        int a = file.indexOf('#');
        if (a != -1) {
            // вызов метода генерации исходного файла
            return generateSourceFile(file.substring(0, a), file.substring(a + 1), attrs, outFile);
        }

        Ref ref = getOutBuilder().getRefResolver().resolveRefInc(file, outFile);

        if (ref == null || ref.getSourceFile() == null) {
            throw new XError("Файл не найден");
        }

        return ref.getSourceFile();
    }

    private SourceFile generateSourceFile(String file, String method, IVariantMap attrs, OutFile outFile) throws Exception {
        Ref ref = getOutBuilder().getRefResolver().resolveRefInc(file, outFile);
        if (ref == null) {
            ref = getOutBuilder().getRefResolver().resolveRefInc(file + ".java", outFile);
            if (ref == null) {
                ref = getOutBuilder().getRefResolver().resolveRefInc(file + ".groovy", outFile);
            }
        }
        if (ref == null || ref.getSourceFile() == null) {
            throw new XError("Файл не найден");
        }
        GroovyFactory gf = getOutBuilder().bean(GroovyFactory.class);
        Object ob = gf.createObject(ref.getSourceFile().getPath());
        if (!(ob instanceof BaseCodeGen)) {
            throw new XError("Класс из файла {0} должен быть наследником от {1}",
                    ref.getSourceFile().getPath(), BaseCodeGen.class.getName());
        }
        BaseCodeGen obg = (BaseCodeGen) ob;

        IVariantMap attrsCopy = new VariantMap();
        attrsCopy.putAll(attrs);
        attrsCopy.remove("file");
        attrsCopy.remove("lang");
        attrsCopy.remove("part");
        attrsCopy.remove("title");

        outFile.addDependFile(ref.getSourceFile()); // в список зависимых, от snippet зависит текст

        return obg.generateSourceFile(method, attrsCopy, outFile, ref.getSourceFile());
    }

    private Snippet getSnippet(SourceFile f) {
        Snippet sn = cache.get(f.getPath());
        if (sn == null) {
            String ext = UtFile.ext(f.getPath());
            Class snCls = getSnippetClass(ext);
            sn = (Snippet) UtClass.createInst(snCls);
            sn.configure(f);
            if (!getOutBuilder().getMode().isServe()) {
                // помещаем в кеш только если не serve
                cache.put(f.getPath(), sn);
            }
        }
        return sn;
    }

    private Class getSnippetClass(String ext) {
        if (ext.equals("java") || ext.equals("groovy") || ext.equals("js")) {
            return JavaSnippet.class;

        } else if (ext.equals("xml") || ext.equals("html") || ext.equals("cfx")) {
            return XmlSnippet.class;

        } else {
            return Snippet.class;
        }
    }

}
