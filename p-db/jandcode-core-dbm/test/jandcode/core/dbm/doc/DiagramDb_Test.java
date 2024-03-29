package jandcode.core.dbm.doc;

import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DiagramDb_Test extends Dbm_Test {

    @Test
    public void test1() throws Exception {
        DomainGroup gr = new DomainGroup(getModel());
        gr.getDomains().addAll(gr.getParentDomains());
        DiagramUtils ut = new DiagramUtils(gr);
        Diagram d = ut.createDiagram(getModel().getConf().getConf("diagram/diag1"));
        assertEquals(d.getName(), "diag1");
        assertEquals(d.getTitle(), "Di1");
        assertEquals(d.getDomains().size(), 2);
    }


}
