package jandcode.mdoc.flexmark;

import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.parser.*;
import com.vladsch.flexmark.util.data.*;
import com.vladsch.flexmark.util.misc.*;
import jandcode.mdoc.flexmark.cm.*;

/**
 * Расширение Cm.
 * Позволяет объявлять специальные команды для последующей
 * обработки. Команда должна быть параграфом.
 * Команды попадают в html как теги:
 * {@code <cm>text</cm>}.
 * Формат:
 * <pre><code>
 * {@literal @@}command-text
 * </code></pre>
 */
public class CmExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    public static Extension create() {
        return new CmExtension();
    }

    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        if (rendererBuilder.isRendererType("HTML")) {
            rendererBuilder.nodeRendererFactory(new CmHtmlRenderer.Factory());
        }
    }

    public void parserOptions(MutableDataHolder options) {
    }

    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.postProcessorFactory(new CmPostProcessor.Factory());
    }

    public void rendererOptions(MutableDataHolder options) {

    }

}
