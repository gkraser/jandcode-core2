package jandcode.flexmark.cm;

import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.html.renderer.*;
import com.vladsch.flexmark.util.data.*;

import java.util.*;

public class CmHtmlRenderer implements NodeRenderer {

    private static String tagName = "cm";

    public static class Factory implements NodeRendererFactory {
        public NodeRenderer apply(DataHolder options) {
            return new CmHtmlRenderer(options);
        }
    }

    public CmHtmlRenderer(DataHolder options) {
    }

    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(CmBlock.class, CmHtmlRenderer.this::render));
        return set;
    }

    private void render(CmBlock node, NodeRendererContext context, HtmlWriter html) {
        html.line();
        html.tag(tagName);
        html.raw(node.getText());
        html.tag('/' + tagName);
        html.line();
    }

}
