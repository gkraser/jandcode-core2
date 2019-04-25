package jandcode.commons.outtable;

import jandcode.commons.*;
import jandcode.commons.rnd.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class OutTable_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        Map<String, Object> m = new HashMap<>();

        Rnd rnd = new Rnd(123);

        for (int i = 0; i < 100; i++) {
            m.put("key-" + i, "value-\n" + i + "-\r" + rnd.text(Rnd.ERN_CHARS, 5, 30, 0) + "*");
        }

        OutTableSaver sv = UtOutTable.createOutTableSaver(new MapOutTable(m));
        sv.setLimit(3);
        System.out.println(sv.save().toString());
    }


}
