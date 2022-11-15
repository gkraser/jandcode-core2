package jandcode.core.apx.dao

import jandcode.core.apx.test.*
import org.junit.jupiter.api.*

class Dict_Test extends Apx_Test {

    @Test
    void resolveDicts1() throws Exception {
        def dd = [
                color: [
                        1: 1,
                        3: 1,
                ],
                dict2: [
                        1: 1,
                        3: 1,
                ]
        ]
        def res = apx.execJsonRpc("api", "apx/dict/resolveDicts", [dd])
        utils.outMap(res)
    }


}
