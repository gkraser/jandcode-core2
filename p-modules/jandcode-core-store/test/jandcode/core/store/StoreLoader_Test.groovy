package jandcode.core.store

import jandcode.core.test.*
import org.junit.jupiter.api.*

class StoreLoader_Test extends App_Test {

    StoreService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(StoreService.class);
    }

    @Test
    public void names() throws Exception {
        println svc.getStoreLoaderNames()
    }

    @Test
    public void csv() throws Exception {
        def ldr = svc.createStoreLoader("csv")
        ldr.load().fromFile(utils.getTestFile("data/csv1.csv"))
        utils.outTable(ldr.getStore())
    }

    @Test
    public void csv_exists_store() throws Exception {
        def ldr = svc.createStoreLoader("csv")
        def st = svc.createStore()
        st.addField("id", "long")
        st.addField("z", "string")
        st.addField("f_date", "date")
        ldr.setStore(st)
        ldr.load().fromFile(utils.getTestFile("data/csv1.csv"))
        utils.outTable(ldr.getStore())
    }

    @Test
    public void xml() throws Exception {
        def ldr = svc.createStoreLoader("xml")
        ldr.load().fromFile(utils.getTestFile("data/xml1.xml"))
        utils.outTable(ldr.getStore())
    }

    @Test
    public void xml_exists_store() throws Exception {
        def ldr = svc.createStoreLoader("xml")
        def st = svc.createStore()
        st.addField("id", "long")
        st.addField("z", "string")
        st.addField("f_date", "date")
        ldr.setStore(st)
        ldr.load().fromFile(utils.getTestFile("data/xml1.xml"))
        utils.outTable(ldr.getStore())
    }

}
