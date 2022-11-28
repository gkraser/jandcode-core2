package jandcode.core.dbm.validate


import jandcode.core.dbm.test.*
import jandcode.core.store.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Validator_Test extends Dbm_Test {

    @Test
    public void test_direct1() throws Exception {
        utils.logOn()
        Store st = mdb.createStore()
        st.addField("f1", "int")
        st.addField("f2", "int")
        StoreRecord rec = st.add(f1: 50)
        //
        mdb.validate(rec, "minmax", [field: "f1", min: 2, max: 20, title: "Поле f1"])
        mdb.validate(rec, "req", [field: "f2", title: "Поле f2"])
        //
        try {
            mdb.validateErrors.checkErrors()
            fail()
        } catch (e) {
            utils.showError(e)
        }
    }

    @Test
    public void record1_title() throws Exception {
        StoreRecord rec = mdb.createStoreRecord("record1", [f1: 50])
        utils.outTable(rec)
        mdb.validate(rec, "minmax", [field: "f1", min: 2, max: 20])
        mdb.validate(rec, "req", [field: "f2"])
        //
        try {
            mdb.validateErrors.checkErrors()
            fail()
        } catch (e) {
            utils.showError(e)
        }
    }

    @Test
    public void record1_field1() throws Exception {
        StoreRecord rec = mdb.createStoreRecord("record1", [f1: 50])
        utils.outTable(rec)
        mdb.validateField(rec, "f1")
        mdb.validateField(rec, "f2")
        //
        try {
            mdb.validateErrors.checkErrors()
            fail()
        } catch (e) {
            utils.showError(e)
        }
    }

    @Test
    public void record1_record1() throws Exception {
        utils.logOn()
        StoreRecord rec = mdb.createStoreRecord("record1", [f1: 50])
        utils.outTable(rec)
        mdb.validateRecord(rec)
        //
        try {
            mdb.validateErrors.checkErrors()
            fail()
        } catch (e) {
            utils.showError(e)
        }
    }

    @Test
    public void record1_record2() throws Exception {
        utils.logOn(utils.getTestFile("data/logback.xml"))
        StoreRecord rec = mdb.createStoreRecord("record1", [f1: 10, f2: 1])
        utils.outTable(rec)
        mdb.validateRecord(rec)
        //
        mdb.validateErrors.checkErrors()
    }


}
