package jandcode.core.dbm.dbstruct

import jandcode.commons.simxml.*
import jandcode.core.dbm.ddl.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

public class DDLScript_Test extends Dbm_Test {

    @Test
    public void grab_script() throws Exception {
        def svc = model.bean(DDLService)
        def script = svc.grabScript()
        SimXml x = new SimXmlNode()
        script.saveToXml(x)
        println x.save().toString()
    }


}
