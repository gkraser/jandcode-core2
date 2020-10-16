package jandcode.core.std;

import jandcode.commons.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AppInfo_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        AppInfo z = app.bean(AppInfo.class);
        //
        assertEquals(z.getMainModule(), "jandcode.core");
        assertEquals(z.getTitle(), "");
        assertFalse(UtString.empty(z.getVersion()));
    }


}
