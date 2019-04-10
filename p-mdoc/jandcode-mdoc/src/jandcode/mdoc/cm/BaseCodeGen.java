package jandcode.mdoc.cm;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.cm.snippet.*;
import jandcode.mdoc.source.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Базовый класс для генераторов исходных файлов для вставки в @@code
 */
public abstract class BaseCodeGen extends BaseOutBuilderMember {

    private Attrs attrs;
    private OutFile outFile;
    private String ext;
    private String methodName;
    private CodeGenTextBuffer textBuffer;
    private SourceFile thisFile;

    public class Attrs extends VariantMapWrap {
        public Attrs(Map map) {
            super(map);
        }

        void errorRequired(String attr) {
            throw new XError("Атрибут [{0}] обязателен для {1}", attr, methodName);
        }

        /**
         * Атрибут-строка с проверкой обязательности.
         *
         * @param name имя аргумента
         * @param req  обязательность аргумента
         */
        public String getString(String name, boolean req) {
            String res = getString(name);
            if (req && UtString.empty(res)) {
                errorRequired(name);
                return "";
            }
            return res;
        }

        /**
         * Файл из атрибута
         *
         * @param name имя атрибута
         */
        public SourceFile getSourceFile(String name) {
            String res = getString(name, true);
            Ref ref = getOutBuilder().getRefResolver().resolveRefInc(res, outFile);
            if (ref == null || ref.getSourceFile() == null) {
                throw new XError("Не найден файл {0}", res);
            }
            return ref.getSourceFile();
        }

    }

    /**
     * Атрибуты для генерации
     */
    public Attrs getAttrs() {
        return attrs;
    }

    /**
     * Выходной файл, внутри которого запущена генерация.
     */
    public OutFile getOutFile() {
        return outFile;
    }

    /**
     * Расширение генерируемого файла
     */
    public String getExt() {
        return ext;
    }

    /**
     * Установить расширение генерируемого файла
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    /**
     * Возвращает кеш, который существует все время работы outBuilder.
     */
    public IVariantMap getCache() {
        String bn = BaseCodeGen.class.getName() + ".$CACHE";
        BeanDef b = getOutBuilder().getBeanFactory().findBean(bn);
        if (b == null) {
            getOutBuilder().getBeanFactory().registerBean(bn, new VariantMap());
        }
        return (IVariantMap) getOutBuilder().bean(bn);
    }

    /**
     * Возвращает кеш для текущего класса, который существует все время работы outBuilder.
     */
    public IVariantMap getCacheThis() {
        String bn = this.getClass().getName() + ".$CACHE";
        BeanDef b = getOutBuilder().getBeanFactory().findBean(bn);
        if (b == null) {
            getOutBuilder().getBeanFactory().registerBean(bn, new VariantMap());
        }
        return (IVariantMap) getOutBuilder().bean(bn);
    }

    /**
     * Сюда выводится текст
     */
    public CodeGenTextBuffer getTextBuffer() {
        return textBuffer;
    }

    /**
     * Вывести текст
     */
    public void outText(Object s) {
        getTextBuffer().outText(s);
    }

    /**
     * Ссылка на этот файл с кодом
     */
    public SourceFile getThisFile() {
        return thisFile;
    }

    /**
     * Получить исходный файл
     *
     * @param ref ссылка на файл. Сначала рассматривается относительно этого файла.
     *            Если не найдена - относительно файла статьи
     * @return ошибка, если не найден
     */
    public SourceFile getSourceFile(String ref) {
        String fn = UtFile.path(getThisFile().getPath()) + "/" + ref;
        SourceFile f = getOutBuilder().getDoc().getSourceFiles().find(fn);
        if (f != null) {
            return f;
        }
        Ref rf = getOutBuilder().getRefResolver().resolveRefInc(ref, getOutFile());
        if (rf == null || rf.getSourceFile() == null) {
            throw new XError("Файл не найден [{0}]", ref);
        }
        return rf.getSourceFile();
    }

    /**
     * Вызов генерации
     *
     * @param methodName имя метода
     * @param attrs      атрибуты для генерации
     * @param outFile    внутри какого outFile встретилось
     * @param thisFile   ссылка на файл с исходником выполняемого класса
     */
    public SourceFile generateSourceFile(String methodName, IVariantMap attrs, OutFile outFile, SourceFile thisFile) throws Exception {
        this.attrs = new Attrs(attrs);
        this.outFile = outFile;
        this.ext = "txt"; // по умолчанию txt
        this.methodName = methodName;
        this.textBuffer = new CodeGenTextBuffer();
        this.thisFile = thisFile;
        String mode = attrs.getString("mode", "out");

        // получаем текст
        Method m = this.getClass().getDeclaredMethod(methodName);
        m.invoke(this);
        String text;
        if (mode.equals("body") || mode.equals("bodyout")) {
            this.ext = "groovy";
            JavaSnippet jsn = new JavaSnippet();
            jsn.configure(this.thisFile);
            SnippetPart bodyPart = jsn.getPart(this.methodName);
            if (bodyPart == null) {
                throw new XError("Странно, но не найден текст тела метода {0}", this.methodName);
            }
            if (mode.equals("body")) {
                text = bodyPart.getText();
            } else {
                StringBuilder sb = new StringBuilder();
                String[] ar = bodyPart.getText().split("\n");
                int num = 0;
                for (String ar1 : ar) {
                    String ar2 = ar1.trim();
                    if (ar2.startsWith("outText(") || ar2.startsWith("this.outText(")) {
                        sb.append(this.textBuffer.getPartAsComment(num));
                        num++;
                    } else {
                        sb.append(ar1);
                    }
                    sb.append("\n");
                }
                text = UtString.trimLast(sb.toString());
                text = text.replaceAll("\\*/\n/\\*", "");  // лишние стыки в коментах
            }

        } else if (mode.equals("out")) {
            text = this.textBuffer.getText();
        } else {
            throw new XError("Неправильное значение mode={0}, ожидается body, bodyout, out", mode);
        }

        // формируем ключ файла
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName()).append("|");
        sb.append(methodName).append("|");
        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            sb.append(entry.getKey()).append("=");
            sb.append(entry.getValue()).append("|");
        }
        String key = UtString.md5Str(sb.toString());

        return new TextSourceFile("code-gen/" +
                this.getClass().getSimpleName() + "/" + methodName + "/" +
                key + "." + this.ext,
                text);
    }

}
