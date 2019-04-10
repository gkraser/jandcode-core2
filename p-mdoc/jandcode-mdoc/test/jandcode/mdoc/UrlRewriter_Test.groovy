package jandcode.mdoc

import jandcode.mdoc.builder.*
import jandcode.mdoc.html.*
import jandcode.mdoc.topic.*
import org.junit.jupiter.api.*

class UrlRewriter_Test extends CustomMDoc_Test {

    @Test
    public void test1() throws Exception {
        def builder = mdoc.buildDoc("doc1")

        String s = """\
  <meta charset="UTF-8">
  <title>Jandcode Core</title>

  <link rel="stylesheet" href="{~ref~:_theme/lib/normalize/normalize.css}"/>
  <link rel="stylesheet" href="{~ref~:_theme/lib/prism/prism.css}"/>
  <link rel="stylesheet" href="{~ref~:_theme/css/admonition.css}"/>
  <link rel="stylesheet" href="{~ref~:_theme/css/style.css}"/>

  <script src="{~ref~:_theme/lib/prism/prism.js}"></script>
  <script src="{~ref~:_theme/js/prism-ext.js}"></script>

"""
        Topic t = builder.doc.topics.find("refs/refs")
        OutFile f = new OutFile('a/v/b/' + t.sourceFile.path + ".html", t.sourceFile, t)

        UrlRewriterPostProcessor r = builder.create(UrlRewriterPostProcessor)
        String s1 = r.process(s, f)
        println s1
    }


}
