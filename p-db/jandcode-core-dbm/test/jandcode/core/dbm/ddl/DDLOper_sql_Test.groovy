package jandcode.core.dbm.ddl

import jandcode.commons.simxml.*
import jandcode.core.dbm.ddl.impl.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

public class DDLOper_sql_Test extends Dbm_Test {

    @Test
    public void test1() throws Exception {
        def svc = model.bean(DDLService)
        DDLOper_sql z = svc.createOperInst("sql")
        z.setName("s1name")
        z.setSqlText("s1text")
        //
        SimXml x = new SimXmlNode()
        z.saveToXml(x)
        //
        DDLOper_sql z2 = svc.createOperInst("sql")
        z2.loadFromXml(x)
        SimXml x2 = new SimXmlNode()
        z2.saveToXml(x2)
        //
        assertEquals(x.save().toString(), x2.save().toString())
        assertEquals(x2.save().toString(), """<?xml version="1.0" encoding="utf-8"?>\n<root name="s1name" type="sql">s1text</root>""")
    }


}
