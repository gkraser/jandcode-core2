package jandcode.mdoc.topic.factory;

import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.util.options.*;
import jandcode.commons.*;
import jandcode.core.*;
import jandcode.flexmark.*;
import jandcode.flexmark.mdtopic.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;
import jandcode.mdoc.topic.impl.*;

public class MdTopicFactory extends BaseComp implements TopicFactory {

    // стандартная
    private FlexmarkEngine flexmarkEngineProd = new FlexmarkEngine();

    // отладочная
    private FlexmarkEngine flexmarkEngineDebug = new FlexmarkEngine() {
        protected void fillOptions(MutableDataSet options) {
            super.fillOptions(options);
            //
            options.set(HtmlRenderer.SOURCE_POSITION_ATTRIBUTE, "source-pos");
            options.set(HtmlRenderer.SOURCE_WRAP_HTML, true);
            options.set(HtmlRenderer.SOURCE_WRAP_HTML_BLOCKS, true);

        }
    };

    public Topic createTopic(SourceFile sourceFile, Doc doc) {
        FlexmarkEngine engine = flexmarkEngineProd;
        if (doc.getMode().isDebug()) {
            engine = flexmarkEngineDebug;
        }

        String sourceText = sourceFile.getText();
        MdTopic mdTopic = engine.createMdTopic(sourceText);

        TopicImpl res = new TopicImpl(sourceFile);
        res.getProps().putAll(mdTopic.getProps());
        res.setTitle(mdTopic.getTitle());
        String titleShort = mdTopic.getProps().get("titleShort");
        if (!UtString.empty(titleShort)) {
            res.setTitleShort(titleShort);
        }

        //
        res.setBody(mdTopic.getBody());
        grabToc(res.getToc(), mdTopic.getToc());

        return res;
    }

    private void grabToc(Toc toToc, MdToc fromToc) {
        toToc.setTitle(fromToc.getTitle());
        toToc.setSection(fromToc.getId());
        for (MdToc from1 : fromToc.getChilds()) {
            Toc to1 = new TocImpl();
            to1.setTopic(toToc.getTopic());
            toToc.addChild(to1);
            grabToc(to1, from1);
        }
    }

}
