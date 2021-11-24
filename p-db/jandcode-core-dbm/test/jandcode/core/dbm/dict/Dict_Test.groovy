package jandcode.core.dbm.dict

import jandcode.commons.*
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

    void prnDictHolder(Store st) {
        DictDataHolder h = st.getDictResolver()
        for (it in h.items) {
            utils.delim(it.dict.name)
            utils.outTable(it.data)
        }
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
        DictData dictData = svc.resolveIds(dict, [5, 6, 7])
        Store st = dictData.data
        utils.outTable(st)
        assertEquals(st.size(), 3)
        assertEquals(st.get(0).getValue("id"), 5)
        assertEquals(st.get(0).getValue("text"), "dict1-text-5")
    }

    @Test
    public void resolve_dicts_for_store() throws Exception {
        Store st = z.createStore()
        st.addField("id", "long")
        st.addField("dict1", "long").setDict("dict1")
        st.addField("dict2", "long").setDict("dict2")
        st.addField("dict22", "string").setDict("dict2")
        st.addField("nodict1", "string")
        for (i in 1..10) {
            if (i == 5) {
                st.add(id: i)
            } else {
                st.add(id: i, dict1: i, dict2: 100 + i, dict22: "1000[${i}]", nodict1: "nodict-${i}")
            }
        }
        //
        svc.resolveDicts(st)
        //
        utils.outTable(st)
        //
        StoreRecord r = st.getById(3)

        assertEquals(r.get("id"), 3)
        assertEquals(r.get("dict1"), 3)
        assertEquals(r.get("dict2"), 103)
        assertEquals(r.get("dict22"), "1000[3]")

        assertEquals(r.getDictValue("dict1"), "dict1-text-3")
        assertEquals(r.getDictValue("dict2"), "dict2-text-103")
        assertEquals(r.getDictValue("dict22"), "dict2-text-1000[3]")


    }

    @Test
    public void conf_dictdata() throws Exception {
        Dict dict = svc.getDicts().get("yes-no")
        DictData dd = svc.getCache().getDictData(dict)
        utils.outTable(dd.data)
        assertEquals(dd.data.size(), 2)
        assertEquals(dd.data.get(0).getValue("text"), "YES")
    }

    @Test
    public void to_json() throws Exception {
        Store st = z.createStore()
        st.addField("id", "long")
        st.addField("dict1", "long").setDict("dict1")
        st.addField("dict2", "long").setDict("dict2")
        for (i in 1..3) {
            if (i == 2) {
                st.add(id: i)
            } else {
                st.add(id: i, dict1: i, dict2: 100 + i)
            }
        }
        //
        svc.resolveDicts(st)
        //
        assertEquals(
                UtJson.toJson(st),
                """{"records":[{"id":1,"dict1":1,"dict2":101},{"id":2},{"id":3,"dict1":3,"dict2":103}],"dictdata":{"dict1":{"1":{"text":"dict1-text-1"},"3":{"text":"dict1-text-3"}},"dict2":{"101":{"text":"dict2-text-101"},"103":{"text":"dict2-text-103"}}}}"""
        )
    }

}
