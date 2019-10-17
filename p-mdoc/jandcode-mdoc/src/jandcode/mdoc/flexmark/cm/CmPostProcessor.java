package jandcode.mdoc.flexmark.cm;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.parser.block.*;
import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.sequence.*;

public class CmPostProcessor extends NodePostProcessor {

    public static final String MARKER = "@@";

    public static class Factory extends NodePostProcessorFactory {

        public Factory() {
            super(false);
            addNodes(Paragraph.class);
        }

        @Override
        public NodePostProcessor apply(Document document) {
            return new CmPostProcessor(document);
        }

    }

    public CmPostProcessor(Document document) {
    }

    public void process(NodeTracker state, Node node) {
        BasedSequence s = node.getChars();
        if (!s.startsWith(MARKER) || s.length() <= MARKER.length()) {
            return; // не наше
        }
        String s1 = s.normalizeEOL().trim().substring(MARKER.length()).replace("\n", " ");

        // делаем блок
        CmBlock b = new CmBlock(s1);
        node.insertAfter(b);
        node.unlink();
    }

}
