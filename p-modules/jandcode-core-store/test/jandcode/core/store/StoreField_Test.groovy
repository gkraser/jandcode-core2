package jandcode.core.store

import jandcode.commons.datetime.impl.*
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


}
