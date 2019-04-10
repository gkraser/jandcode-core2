package jandcode.mdoc

import jandcode.mdoc.topic.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Doc_Test extends CustomMDoc_Test {

    @Test
    void doc1() throws Exception {
        Doc doc = mdoc.loadDoc("doc1");
        mdoc.printDoc(doc);
    }

    @Test
    void real1() throws Exception {
        Doc doc = mdoc.loadDoc("real1");
        mdoc.printDoc(doc);
    }

    @Test
    void topicFind1() throws Exception {
        Doc doc = mdoc.loadDoc("doc1");
        Topic t1 = doc.getTopics().find("code1")
        Topic t2 = doc.getTopics().find("///code1.md///")
        assertNotNull(t1)
        assertSame(t1, t2)
    }


}
