package jandcode.core.store

import jandcode.commons.datetime.impl.*
import jandcode.core.store.std.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class StoreField_Test extends App_Test {

    StoreService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(StoreService.class);
    }

    public StoreRecord createRec(String fieldType) {
        def st = svc.createStore()
        st.addField("f", fieldType, 10)
        return st.add()
    }

    @Test
    public void test_blob() throws Exception {
        def v
        def rec = createRec("blob")

        v = rec["f"]
        assertEquals(v.class, byte[])

        rec.setValue("f", "123")
        v = rec["f"]
        assertEquals(v.class, byte[])
        assertEquals(v.toString(), "[49, 50, 51]")
        println rec["f"]
    }

    @Test
    public void test_date() throws Exception {
        def v
        def rec = createRec("date")

        v = rec["f"]
        assertEquals(v.class, XDateImpl)

        rec["f"] = "2012-12-30"
        v = rec["f"]
        assertEquals(v.class, XDateImpl)
        assertEquals(v.toString(), "2012-12-30")
        println rec["f"]

        rec["f"] = "2012-12-30T12:13:14"
        v = rec["f"]
        assertEquals(v.class, XDateImpl)
        assertEquals(v.toString(), "2012-12-30")
        println rec["f"]
    }

    @Test
    public void test_datetime() throws Exception {
        def v
        def rec = createRec("datetime")

        v = rec["f"]
        assertEquals(v.class, XDateTimeImpl)

        rec["f"] = "2012-12-30"
        v = rec["f"]
        assertEquals(v.class, XDateTimeImpl)
        assertEquals(v.toString(), "2012-12-30")
        println rec["f"]

        rec["f"] = "2012-12-30T12:13:14"
        v = rec["f"]
        assertEquals(v.class, XDateTimeImpl)
        assertEquals(v.toString(), "2012-12-30T12:13:14")
        println rec["f"]
    }

    @Test
    public void test_double_scale() throws Exception {
        def v
        def rec = createRec("double")

        v = rec["f"]
        assertEquals(v.class, Double)

        rec["f"] = 123.456789
        assertEquals(rec["f"], 123.456789)

        rec.store.getField("f").setScale(2)
        assertEquals(rec["f"], 123.46)

        rec.store.getField("f").setScale(StoreField.NO_SCALE)
        assertEquals(rec["f"], 123.456789)
    }

    @Test
    public void test_calc() throws Exception {
        def st = svc.createStore()
        st.addField("f", "string")
        st.addField("c1", "string").calc { f, r ->
            if (r.isValueNull("f")) {
                return null
            }
            return r.getString("f") + "-calc"
        }
        st.add()
        st.add(f: 'v1')
        st.add(f: 'z1')
        utils.outTable(st)
        st.get(1).setValue("f", "v11")
        utils.outTable(st)

        assertEquals(st.get(0).getValue("c1"), null)
        assertEquals(st.get(1).getValue("c1"), "v11-calc")
        assertEquals(st.get(2).getValue("c1"), "z1-calc")
    }

    @Test
    public void test_calc_guid() throws Exception {
        def st = svc.createStore()
        st.addField("c1", "string").calc(new StoreCalcField_guid())
        st.add()
        utils.outTable(st)
        def v1 = st.get(0).getValue("c1")
        st.get(0).setValue("c1", "v11")
        utils.outTable(st)
        def v2 = st.get(0).getValue("c1")
        assertEquals(v1, v2)
        assertNotNull(v1)
    }

    @Test
    public void test_calc_id() throws Exception {
        def st = svc.createStore()
        st.addField("c1", "long").calc(new StoreCalcField_id())
        st.addField("c2", "long").calc(new StoreCalcField_id())
        st.add()
        st.add()
        utils.outTable(st)
        def v1 = st.get(0).getValue("c1")
        st.get(0).setValue("c1", "v11")
        utils.outTable(st)
        def v2 = st.get(0).getValue("c1")
        assertEquals(v1, v2)
        assertNotNull(v1)
        assertEquals(st.get(0).getValue("c1"), 1)
        assertEquals(st.get(0).getValue("c2"), 2)
    }

    @Test
    public void test_calc_id_registred() throws Exception {
        def st = svc.createStore()
        st.addField("c1", "long").calc("id")
        st.addField("c2", "string").calc("guid")
        st.add()
        st.add()
        utils.outTable(st)
        utils.outTable(st)
    }

}
