package jandcode.core.apx;

import jandcode.core.apx.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class JsonRpc_Test extends Apx_Test {

    @Test
    public void invoke2() throws Exception {
        utils.logOn();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> m = apx.execJsonRpc("api", "dao1/str", List.of("string-" + i));
            utils.outMap(m);
        }
    }

}
