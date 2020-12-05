package jandcode.core.dao

import jandcode.commons.*
import jandcode.core.dao.data.*
import jandcode.core.dao.data.recursive_pak.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class DaoHolder_Test extends App_Test {

    void prnHolder(DaoHolder h) {
        utils.delim("rules")
        for (z in h.getRules()) {
            println "${UtString.padRight(z.mask, 37)} -> ${z.invoker}"
        }
        utils.delim("items")
        for (z in h.getItems()) {
            println "${UtString.padRight(z.name, 40)} ${h.resolveDaoInvokerName(z)}"
        }
        utils.delim()
    }

    @Test
    public void test1() throws Exception {
        DaoService svc = app.bean(DaoService.class)
        DaoHolder h = svc.getDaoHolder("test1");
        //
        for (it in h.getItems()) {
            println "${it.name} -> ${it.methodDef.method}"
        }

        assertSame(
                h.items.get("dao2/sum").methodDef,
                h.items.get("pfx/dao2/sum").methodDef,
        )

        assertEquals(
                h.items.get("recursive_pak/dao11/sum").methodDef.cls,
                Dao11.class
        )

        assertEquals(
                h.items.get("d1/p1").methodDef.cls,
                Dao11.class
        )

        assertEquals(
                h.items.get("d1/p1").methodDef.method.name,
                "sum"
        )

        assertEquals(
                h.items.get("pfx/c1/p1").methodDef.cls,
                Dao11.class
        )

        assertEquals(
                h.items.get("pfx/fold1/fold2/f1").methodDef.cls,
                Dao11.class
        )

        assertEquals(
                h.items.get("pfx/recursive_pak/mySuper2/test1").methodDef.cls,
                MySuper2_Dao.class
        )

        assertEquals(
                h.items.get("pfx/mySuper1/test1").methodDef.cls,
                MySuper1Dao.class
        )

        assertEquals(
                h.items.get("flat/mySuper2/test1").methodDef.cls,
                MySuper2_Dao.class
        )

    }

    @Test
    public void invoke1() throws Exception {
        DaoService svc = app.bean(DaoService.class)
        DaoHolder h = svc.getDaoHolder("test1");
        //
        assertEquals(h.resolveDaoInvokerName(h.getItems().get("d1/p1")), "filters1")
        //
        Object a = h.invokeDao("d1/p1", 5, 6)
        assertEquals(a, 11)
    }

    @Test
    public void check_rules() throws Exception {
        DaoService svc = app.bean(DaoService.class)
        DaoHolder h = svc.getDaoHolder("check-rules");
        //
        prnHolder(h)
        //
        assertEquals(h.resolveDaoInvokerName(h.items.get("dao1/sum")), "global")
        assertEquals(h.resolveDaoInvokerName(h.items.get("mySuper1/test1")), "t1")
        assertEquals(h.resolveDaoInvokerName(h.items.get("recursive_pak/mySuper2/test1")), "x2")
    }


}
