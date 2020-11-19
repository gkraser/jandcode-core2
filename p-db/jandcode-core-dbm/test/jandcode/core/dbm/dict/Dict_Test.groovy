package jandcode.core.dbm.dict

import jandcode.core.dbm.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class Dict_Test extends App_Test {

    DbmDbTestSvc z = testSvc(DbmDbTestSvc)

    DictService svc

    void setUp() throws Exception {
        super.setUp()
        //
        svc = z.model.bean(DictService)
    }

    @Test
    public void test1() throws Exception {
        for (dict in svc.getDicts()) {
            println "${dict.name}"
        }
    }

    @Test
    public void abstract_dict() throws Exception {
        assertEquals(svc.dicts.find("dummy"), null)
    }

    @Test
    public void name_dict() throws Exception {
        assertEquals(svc.dicts.get("dict1").getName(), "dict1")
    }


}
