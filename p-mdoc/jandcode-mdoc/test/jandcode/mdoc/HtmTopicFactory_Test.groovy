package jandcode.mdoc

import jandcode.mdoc.source.*
import jandcode.mdoc.topic.*
import org.junit.jupiter.api.*

class HtmTopicFactory_Test extends CustomMDoc_Test {

    @Test
    public void test1() throws Exception {
        Doc doc = mdoc.loadDoc("doc1")
        TextSourceFile f = new TextSourceFile("a.htm", """\
<mdoc-props>
    title=Qaz
</mdoc-props>

qqq
                 
<h2>tit-1</h2>

hello

<h2>tit-2</h2>

aa2
<h2>tit-3</h2>

""")
        Topic t = doc.createTopic(f)
        println "TITLE=[${t.title}]"
        println mdoc.tocToStr(t.toc)
        utils.delim()
        println t.body
    }


}
