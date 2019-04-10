package jandcode.flexmark;

import com.vladsch.flexmark.*;
import com.vladsch.flexmark.ext.admonition.*;
import com.vladsch.flexmark.ext.attributes.*;
import com.vladsch.flexmark.ext.definition.*;
import com.vladsch.flexmark.ext.gfm.strikethrough.*;
import com.vladsch.flexmark.ext.tables.*;
import com.vladsch.flexmark.ext.yaml.front.matter.*;
import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.parser.*;
import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.options.*;
import jandcode.commons.error.*;
import jandcode.flexmark.mdtopic.*;
import jandcode.flexmark.mdtopic.impl.*;

import java.io.*;
import java.util.*;

/**
 * markdown парзер и генератор html.
 * Обертка вокруг flexmark вместе с расширениями, настроенными по умолчанию.
 */
public class FlexmarkEngine {

    private Parser parser;
    private HtmlRenderer renderer;
    private List<Extension> extensions;
    private MutableDataSet options;

    /**
     * Заполняет список экземплярами расширений, которые будут использоваться.
     */
    protected void fillExtensions(List<Extension> lst) {
        lst.add(StrikethroughExtension.create());
        lst.add(TablesExtension.create());
        lst.add(YamlFrontMatterExtension.create());
        lst.add(DefinitionExtension.create());
        lst.add(AttributesExtension.create());
        lst.add(AdmonitionExtension.create());

        // jandcode extensions
        lst.add(CmExtension.create());
        lst.add(CodeBlockEx.create());
    }

    /**
     * Заполняет опции
     */
    protected void fillOptions(MutableDataSet options) {
        options.set(HtmlRenderer.RENDER_HEADER_ID, true);
        options.set(HtmlRenderer.GENERATE_HEADER_ID, true);
        //
        options.set(Parser.EXTENSIONS, getExtensions());
    }

    /**
     * Опции
     */
    public MutableDataSet getOptions() {
        if (options == null) {
            MutableDataSet tmp = new MutableDataSet();
            fillOptions(tmp);
            options = tmp;
        }
        return options;
    }

    /**
     * Экземпляры расширений
     */
    public List<Extension> getExtensions() {
        if (extensions == null) {
            ArrayList<Extension> tmp = new ArrayList<>();
            fillExtensions(tmp);
            extensions = tmp;
        }
        return extensions;
    }

    /**
     * Экземпляр парзера
     */
    public Parser getParser() {
        if (parser == null) {
            parser = Parser.builder(getOptions()).build();
        }
        return parser;
    }

    /**
     * Экземпляр рендерера
     */
    public HtmlRenderer getRenderer() {
        if (renderer == null) {
            renderer = HtmlRenderer.builder(getOptions()).build();
        }
        return renderer;
    }

    /**
     * Создать объект MdTopic из текста markdown
     */
    public MdTopic createMdTopic(String text) {
        if (text != null) {
            // без этого неправильно считаются номера строк
            text = text.replace("\r", "");
        }
        Document document = getParser().parse(text);
        return new MdTopicImpl(document, getRenderer());
    }

    /**
     * Создать объект MdTopic из Reader с текстом markdown
     */
    public MdTopic createMdTopic(Reader reader) {
        Document document = null;
        try {
            document = getParser().parseReader(reader);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
        return new MdTopicImpl(document, getRenderer());
    }

}
