package jandcode.core.store

import jandcode.commons.datetime.*
import jandcode.commons.reflect.*
import jandcode.commons.rnd.*
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
        st.getField("id").dict("dict1")
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

        st.add(id: 111, s1: 's11,s11-1,s11-2', s2: 's22', s3: 's33')
        //
        utils.outTable(st)
        utils.outTable(st.get(0))
    }

    @Test
    public void sort1() throws Exception {
        def st = svc.createStore()
        st.addField("id", "long")
        st.addField("s1", "string")
        st.addField("i2", "int")
        st.addField("d3", "date")
        st.addField("i4", "int")

        Rnd rnd = Rnd.create(123)
        XDateTime today = XDateTime.today()

        for (i in 1..10) {
            st.add(
                    id: i,
                    s1: rnd.text(Rnd.ERN_CHARS, 5, 10, 5),
                    i2: rnd.num(20, 50),
                    d3: today.addDays(-rnd.num(20, 50)),
                    i4: rnd.num(2, 4),
            )
        }
        utils.outTable(st)
        //
        st.sort("i4,i2")
        utils.outTable(st)
        //
        st.sort("*i4,*i2")
        utils.outTable(st)
        //

    }

    ////// add from instance

    class InstanceRec {
        long id
        String text
        XDate date1
        XDateTime datetime1

        @FieldProps(dict = "dict1")
        long dict1
    }

    @Test
    void add_rec_from_instance() {
        def st = svc.createStore()
        st.addField("id", "long")
        st.addField("text", "string")
        st.addField("date1", "date")
        st.addField("datetime1", "datetime")

        def r = new InstanceRec()
        r.id = 123
        r.text = "hello"
        r.date1 = XDate.create(2001, 01, 30)
        r.datetime1 = XDateTime.create(2001, 01, 29, 10, 11, 12)
        st.add(r)
        utils.outTable(st)

        def rec = st.get(0)
        assertEquals(rec.getLong("id"), 123)
        assertEquals(rec.getString("text"), "hello")
        assertEquals(rec.getDate("date1"), XDate.create(2001, 01, 30))
        assertEquals(rec.getDateTime("datetime1"), XDateTime.create(2001, 01, 29, 10, 11, 12))
    }

    //////

    @Test
    public void field_by_class() throws Exception {
        Store st = svc.createStore()
        st.addField("id", long)
        st.addField("date", XDate)
        //
        st.add(id: 1, date: XDate.create(2000, 1, 2))
        //
        utils.outTable(st)
    }

    @Test
    public void store_by_class() throws Exception {
        Store st = svc.createStore(InstanceRec)

        def r = new InstanceRec()
        r.id = 123
        r.text = "hello"
        r.date1 = XDate.create(2001, 01, 30)
        r.datetime1 = XDateTime.create(2001, 01, 29, 10, 11, 12)
        r.dict1 = 135
        st.add(r)
        utils.outTable(st)

        def rec = st.get(0)
        assertEquals(rec.getLong("id"), 123)
        assertEquals(rec.getString("text"), "hello")
        assertEquals(rec.getDate("date1"), XDate.create(2001, 01, 30))
        assertEquals(rec.getDateTime("datetime1"), XDateTime.create(2001, 01, 29, 10, 11, 12))
        assertEquals(rec.getLong("dict1"), 135)

        assertEquals(st.getField("dict1").getDict(), "dict1")
        assertEquals(st.getField("id").getDict(), null)

    }

    //////

    @Test
    public void fieldsMapper1() throws Exception {
        Store st = svc.createStore(InstanceRec)
        st.withFieldsMapper(
                "_t1": "text",
                "_d1": "dict1",
        )
        //
        assertEquals(st.getCountFields(), 5)
        assertEquals(st.hasField("text"), true)
        assertEquals(st.hasField("_t1"), false)
        assertEquals(st.findField("text").getName(), "text")
        assertEquals(st.findField("_t1").getName(), "text")
        assertEquals(st.findField("_d1").getName(), "dict1")
        assertEquals(st.findField("!_d1"), null)
        //
        StoreRecord rec = st.add(
                text: "T1",
                _t1: "T1-1",
                _d1: 123,
                __x: "qqq"
        )
        //
        assertEquals(rec.getString("text"), "T1-1")
        assertEquals(rec.getString("dict1"), "123")
        assertEquals(rec.getString("_d1"), "123")
        try {
            assertEquals(rec.getString("__x"), "")
            fail("поле __x не должно существовать")
        } catch (ignored) {
        }
        //
    }

    @Test
    public void uniqueValues1() throws Exception {
        Store st = svc.createStore()
        st.addField("f1", "string")

        st.add(f1: "z1")
        st.add(f1: "z2")
        st.add()
        st.add(f1: "z4")
        st.add(f1: "z2")
        st.add(f1: "z1")

        def v = st.getUniqueValues("f1")

        assertEquals(st.size(), 6)
        assertEquals(v.size(), 3)
        assertEquals(v.toString(), "[z1, z2, z4]")
    }

    @Test
    public void null_values() throws Exception {
        Store st = svc.createStore()
        st.addField("f1", "string")
        st.addField("f2", "long")
        st.addField("f3", "double")
        st.addField("f3_nan", "double")
        st.addField("f3_inf", "double")
        //
        double zero = 0.0
        double inf = 1.0 / zero
        def rec = st.add(
                f3_nan: Double.NaN,
                f3_inf: inf
        )
        //
        assertEquals(rec.getValue("f1"), "")
        assertEquals(rec.getValue("f2"), 0)
        assertEquals(rec.getValue("f3"), 0.0)
        assertEquals(rec.getValue("f3_nan"), 0.0)
        assertEquals(rec.getValue("f3_inf"), 0.0)
        IRawRecord rr = rec
        assertTrue(Double.isInfinite(rr.getRawValue(4))) // f3_inf
        //
        assertNull(rec.getValueNullable("f1"))
        assertNull(rec.getValueNullable("f2"))
        assertNull(rec.getValueNullable("f3"))
        assertNull(rec.getValueNullable("f3_nan"))
        assertNull(rec.getValueNullable("f3_inf"))
        //
        assertTrue(rec.isValueNull("f1"))
        assertTrue(rec.isValueNull("f2"))
        assertTrue(rec.isValueNull("f3"))
        assertTrue(rec.isValueNull("f3_nan"))
        assertTrue(rec.isValueNull("f3_inf"))
        //
    }

}
