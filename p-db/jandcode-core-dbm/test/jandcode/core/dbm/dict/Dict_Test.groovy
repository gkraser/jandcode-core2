package jandcode.core.dbm.dict

import jandcode.core.dbm.*
import jandcode.core.store.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

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

    @Test
    public void resolve_ids() throws Exception {
        Dict dict = svc.dicts.get('dict1')
        DictData dictData = dict.resolveIds([5, 6, 7])
        Store st = dictData.data
        utils.outTable(st)
        assertEquals(st.size(), 3)
        assertEquals(st.get(0).getValue("id"), 5)
        assertEquals(st.get(0).getValue("text"), "dict1-text-5")
    }


}
