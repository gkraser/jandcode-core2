package jandcode.flexmark.codeblock;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.html.renderer.*;
import com.vladsch.flexmark.util.options.*;
import jandcode.commons.*;
import jandcode.commons.attrparser.*;
import jandcode.commons.variant.*;

import java.util.*;

import static com.vladsch.flexmark.html.renderer.CoreNodeRenderer.*;

public class CodeBlockExRenderer implements NodeRenderer {

    public static class Factory implements NodeRendererFactory {
        public NodeRenderer create(final DataHolder options) {
            return new CodeBlockExRenderer(options);
        }
    }

    public CodeBlockExRenderer(DataHolder options) {
    }

    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(FencedCodeBlock.class, CodeBlockExRenderer.this::render));
        return set;
    }

    /**
     * Этот метод перекроет оригинальный render
     */
    private void render(FencedCodeBlock node, NodeRendererContext context, HtmlWriter html) {

        // извлекаем язык и атрибуты
        IVariantMap attrs = new VariantMap();
        String langName = "txt";

        String infoStr = node.getInfo().toString().trim();
        if (infoStr.length() > 0) {
            int a = infoStr.indexOf(' ');
            if (a == -1) {
                langName = infoStr;
            } else {
                langName = infoStr.substring(0, a);
                AttrParser prs = new AttrParser();
                prs.loadFrom(infoStr.substring(a + 1));
                attrs.putAll(prs.getResult());
            }
        }

        String title = attrs.getString("title");
        attrs.remove("title");

        // выводим

        html.line();

        html.attr("class", "codeblock");
        html.attr("data-language", langName);
        for (Map.Entry<String, Object> en : attrs.entrySet()) {
            html.attr(en.getKey(), UtCnv.toString(en.getValue()));
        }
        html.withAttr().tag("div");

        if (!UtString.empty(title)) {
            html.attr("class", "codeblock--title");
            html.withAttr().tag("div");
            html.text(title);
            html.withAttr().tag("/div");
        }

        html.attr("class", "codeblock--content");
        html.withAttr().tag("div");

        html.srcPosWithTrailingEOL(node.getChars()).withAttr().tag("pre").openPre();

        String langClass = context.getHtmlOptions().languageClassPrefix + langName;
        html.attr("class", langClass);

        html.srcPosWithEOL(node.getContentChars()).withAttr(CODE_CONTENT).tag("code");
        html.text(node.getContentChars().normalizeEOL());
        html.tag("/code");
        html.tag("/pre").closePre();
        html.lineIf(context.getHtmlOptions().htmlBlockCloseTagEol);

        html.tag("/div");
        html.tag("/div");

    }

}
