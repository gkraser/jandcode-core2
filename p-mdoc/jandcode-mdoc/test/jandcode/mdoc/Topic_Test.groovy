package jandcode.mdoc

import jandcode.mdoc.topic.*
import org.junit.jupiter.api.*

class Topic_Test extends CustomMDoc_Test {

    Doc doc

    void setUp() throws Exception {
        super.setUp()
        //
        doc = mdoc.loadDoc("doc1")
    }

    @Test
    public void test_codeBlock1() throws Exception {
        Topic t = doc.topics.get("code1")
        println t.body
    }

    @Test
    public void title_in_yaml1() throws Exception {
        Topic t = doc.topics.get("yaml1")
        println t.title
        println t.titleShort
        utils.delim()
        println t.body
    }

}
