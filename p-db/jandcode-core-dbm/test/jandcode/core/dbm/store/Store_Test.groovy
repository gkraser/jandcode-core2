package jandcode.core.dbm.store

import jandcode.core.dbm.*
import jandcode.core.store.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

public class Store_Test extends App_Test {

    DbmDbTestSvc z = testSvc(DbmDbTestSvc)

    @Test
    public void store_has_dict_data() throws Exception {
        Store st = z.model.bean(ModelStoreService).createStore()
        assertEquals(st.getDictResolver() instanceof DictData, true)
    }

}
