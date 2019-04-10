package jandcode.flexmark;

import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.util.options.*;
import jandcode.flexmark.codeblock.*;

/**
 * Расширение для FencedCodeBlock.
 * Оборачивает code в div.
 * Все что после языка - атрибуты.
 * Атрибут title - заголовок.
 */
public class CodeBlockEx implements HtmlRenderer.HtmlRendererExtension {

    public static CodeBlockEx create() {
        return new CodeBlockEx();
    }

    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        if (rendererBuilder.isRendererType("HTML")) {
            rendererBuilder.nodeRendererFactory(new CodeBlockExRenderer.Factory());
        }
    }

    public void rendererOptions(MutableDataHolder options) {
    }

}
