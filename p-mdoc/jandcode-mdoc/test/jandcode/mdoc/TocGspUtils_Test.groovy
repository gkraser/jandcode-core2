package jandcode.mdoc

import jandcode.mdoc.builder.*
import jandcode.mdoc.groovy.*
import jandcode.mdoc.gsp.*
import jandcode.mdoc.source.*
import jandcode.mdoc.topic.*
import org.junit.jupiter.api.*

class TocGspUtils_Test extends CustomMDoc_Test {

    OutBuilder builder
    GspTemplateContext ctx

    public void prepare(String docName) {
        builder = mdoc.buildDoc(docName)
        GroovyFactory gspFactory = builder.bean(GroovyFactory)
        ctx = new GspTemplateContext(gspFactory, new OutFile("pak/text.html", new TextSourceFile("pak/text.md", ""), null), builder)
    }

    @Test
    public void toc1() throws Exception {
        prepare("real1")
        //
        TocGspUtils ut = new TocGspUtils(ctx)

        utils.delim()

        Topic t = builder.doc.topics.get("jc/base")
        println ut.makeToc(t.toc)

    }

    @Test
    public void breadcrumb1() throws Exception {
        prepare("real1")
        //

        TocGspUtils ut = new TocGspUtils(ctx)
        //println ut.makeToc(autoToc)

        utils.delim()

        Topic t = builder.doc.topics.get("jc/base")
        println ut.makeBreadcrumb(builder.toc, t)

    }


}
