package jandcode.store

import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

public class Store_Test extends App_Test {

    StoreService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(StoreService.class);
    }

    public Store createStore() {
        def st = svc.createStore()
        st.addField("id", "long")
        st.addField("s1", "string")
        st.addField("s2", "string")
        st.addField("s3", "string")
        return st
    }

    @Test
    public void remove1() throws Exception {
        Store st = createStore();

        assertEquals(st.getCountFields(), 4)
        assertEquals(st.getField("id").getIndex(), 0);
        assertEquals(st.getField("s3").getIndex(), 3);

        st.removeField("s2");

        assertEquals(st.getField("id").getIndex(), 0);
        assertEquals(st.getField("s3").getIndex(), 2);
    }


    @Test
    public void test1() throws Exception {
        Store st = svc.createStore();

        st.addField("id", "long");
        st.addField("s1", "string");

        assertEquals(st.size(), 0);

        StoreRecord r;

        r = st.add();
        r.setValue("id", 123);
        r.setValue("s1", "321");
        assertEquals(r.getValues().toString(), "[id:123, s1:321]")

        st.addField("s2", "string");
        r.setValue("s2", "567");
        System.out.println(r.getValues());
        assertEquals(r.getValues().toString(), "[id:123, s1:321, s2:567]")
    }

    @Test
    public void test_groovyWay() throws Exception {
        def st = createStore()
        def rec = st.add()

        rec.id = 12
        assertEquals(rec.id, 12)

        rec['s1'] = "x12"
        assertEquals(rec["s1"], "x12")

        rec = st.add(
                id: 13,
                s1: "z21"
        )

        assertEquals(rec.id, 13)
        assertEquals(rec["s1"], "z21")
    }

    class DictResolver implements IStoreDictResolver {

        Object getDictValue(String dictName, Object key, String dictFieldName) {
            if (dictName.startsWith("dict")) {
                if (dictFieldName == null) {
                    dictFieldName = "name-default"
                }
                if (dictFieldName.startsWith("n")) {
                    return dictFieldName + "-" + key
                }
            }
            return null
        }
    }

    @Test
    public void dict1() throws Exception {
        Store st = createStore()
        st.setDictResolver(new DictResolver())
        st.getField("id").setDict("dict1")
        st.getField("s1").setDict("dict2")
        st.getField("s2").setDict("no-dict")
        //
        StoreRecord r = st.add(id: 111, s1: 's11', s2: 's22', s3: 's33')

        assertEquals(r.getDictValue("id"), "name-default-111")
        assertEquals(r.getDictValue("s1"), "name-default-s11")
        assertEquals(r.getDictValue("s2"), null)
        assertEquals(r.getDictValue("s3"), null)

        assertEquals(r.getDictValue("id", "n1"), "n1-111")
        assertEquals(r.getDictValue("s1", "n1"), "n1-s11")
        assertEquals(r.getDictValue("s2", "n1"), null)
        assertEquals(r.getDictValue("s3", "n1"), null)

        assertEquals(r.getDictValue("id", "x1"), null)
        assertEquals(r.getDictValue("s1", "x1"), null)
        assertEquals(r.getDictValue("s2", "x1"), null)
        assertEquals(r.getDictValue("s3", "x1"), null)

        //
        utils.outTable(st)
        utils.outTable(st.get(0))
    }

}
