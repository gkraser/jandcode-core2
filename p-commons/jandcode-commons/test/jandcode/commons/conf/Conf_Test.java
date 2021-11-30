package jandcode.commons.conf;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Conf_Test extends CustomConf_Test {

    @Test
    public void create1() throws Exception {
        Conf x = Conf.create();
    }

    @Test
    public void set1() throws Exception {
        Conf x = Conf.create();
        x.setValue("str", "s1");
        x.setValue("int", 1);
        x.setValue("bool", true);
        x.setValue("date", XDateTime.create("2017-01-30"));
        x.setValue("datetime", XDateTime.create("2017-01-30T12:13:14"));

        conf.printConf(x);
        assertEquals(x.getString("int"), "1");
        assertEquals(conf.toMap(x).toString(),
                "{str=s1, int=1, bool=true, date=2017-01-30, datetime=2017-01-30T12:13:14}");
    }

    @Test
    public void caseSent1() throws Exception {
        Conf x = Conf.create();
        x.setValue("str", "s1");
        x.setValue("sTr", "s2");

        conf.printConf(x);
        assertEquals(x.getString("str"), "s1");
        assertEquals(x.getString("sTr"), "s2");
    }

    @Test
    public void objProp1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a", 1);

        // как сделать свойство-объект:

        // from map
        x.setValue("fromMap", UtCnv.toMap("k1", "v1", "k2", "v2"));

        // from conf
        Conf x1 = Conf.create();
        x1.setValue("b", 2);
        x.setValue("fromConf", x1);

        // on fly
        x1 = x.findConf("onFly", true);
        x1.setValue("c", 3);
        x1 = x.findConf("onFly", true);
        x1.setValue("c1", 33);

        //
        conf.printConf(x);
        conf.printConf(x.getConf("fromConf"));
        assertEquals(x.keySet().toString(), "[a, fromMap, fromConf, onFly]");
        assertEquals(conf.toMap(x).toString(), "" +
                "{a=1, fromMap={k1=v1, k2=v2}, " +
                "fromConf={b=2}, onFly={c=3, c1=33}}");
        // особенность!
        assertEquals(x.toString(), conf.toMap(x).toString());
    }

    @Test
    public void objList1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a", 1);

        // как сделать свойство-список:

        // from simple list
        x.setValue("fromSimpleList", Arrays.asList(1, 2, 3));

        // from simple list
        x.setValue("fromObjList", Arrays.asList(
                UtCnv.toMap("a", 1),
                UtCnv.toMap("b", 2)
        ));

        x.setValue("#", 1);

        //
        conf.printConf(x);
        assertEquals(conf.toMap(x).toString(), "" +
                "{a=1, fromSimpleList={#0=1, #1=2, #2=3}, " +
                "fromObjList={#0={a=1}, #1={b=2}}, #3=1}");
    }

    @Test
    public void confList1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a", 1);
        x.setValue("b", Conf.create(UtCnv.toMap("bb", "1")));
        x.setValue("c", Conf.create(UtCnv.toMap("cc", "1")));
        x.setValue("d", 1);

        Collection<Conf> lst = x.getConfs();

        conf.printConf(x);
        Conf xx = Conf.create(lst);
        conf.printConf(xx);

        assertEquals(conf.toMap(x).toString(), "" +
                "{a=1, b={bb=1}, c={cc=1}, d=1}");

        assertEquals(conf.toMap(xx).toString(), "" +
                "{#0={bb=1}, #1={cc=1}}");

    }

    @Test
    public void findPath1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a", 1);
        x.setValue("b", 2);

        assertNull(x.findConf("c"));

        x.findConf("b", true);
        assertNull(x.findConf("b/d/e"));
        x.findConf("b/d////e", true);

        conf.printConf(x);
        assertEquals(conf.toMap(x).toString(), "" +
                "{a=1, b={d={e={}}}}");
    }

    @Test
    public void toString1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a", 1);
        x.setValue("b", 2);
        assertEquals(x.toString(), "{a=1, b=2}");
    }

    @Test
    public void join_addStr1() throws Exception {
        Conf x1 = Conf.create();
        x1.setValue("a", 1);
        Conf x2 = Conf.create();
        x2.setValue("b", 2);
        x1.join(x2);
        assertEquals(conf.toMap(x1).toString(), "{a=1, b=2}");
    }

    @Test
    public void join_overrideStr1() throws Exception {
        Conf x1 = Conf.create();
        x1.setValue("a", 1);
        Conf x2 = Conf.create();
        x2.setValue("a", 2);
        x1.join(x2);
        assertEquals(conf.toMap(x1).toString(), "{a=2}");
    }

    @Test
    public void join_overrideStr_noname1() throws Exception {
        Conf x1 = Conf.create();
        x1.setValue("#", 1);
        Conf x2 = Conf.create();
        x2.setValue("#", 2);
        x1.join(x2);
        assertEquals(conf.toMap(x1).toString(), "{#0=1, #1=2}");
    }

    @Test
    public void join_overrideStrWithObj1() throws Exception {
        Conf x1 = Conf.create();
        x1.setValue("a", 1);
        Conf x2 = Conf.create();
        x2.setValue("a", Conf.create(UtCnv.toMap("s", 1)));
        x1.join(x2);
        assertEquals(conf.toMap(x1).toString(), "{a={s=1}}");
    }

    @Test
    public void join_joinObj1() throws Exception {
        Conf x1 = Conf.create();
        x1.setValue("a", Conf.create(UtCnv.toMap("s", 1)));
        Conf x2 = Conf.create();
        x2.setValue("a", Conf.create(UtCnv.toMap("d", 2)));
        x1.join(x2);
        assertEquals(conf.toMap(x1).toString(), "{a={s=1, d=2}}");
    }

    @Test
    public void link1() throws Exception {
        Conf x1 = Conf.create();
        Conf x2 = Conf.create();
        x2.setValue("a", 1);
        x1.setValue("link", x2);

        // change in inked
        x2.setValue("b", 2);

        conf.printConf(x1);
        assertEquals(conf.toMap(x1).toString(), "{link={a=1, b=2}}");
    }

    @Test
    public void no_link_withJoin1() throws Exception {
        Conf x1 = Conf.create();
        Conf x2 = Conf.create();
        x2.setValue("a", 1);

        Conf x3 = x1.findConf("nolink", true);
        x3.join(x2);

        // change in inked
        x2.setValue("b", 2);

        conf.printConf(x1);
        assertEquals(conf.toMap(x1).toString(), "{nolink={a=1}}");
    }

    @Test
    public void clone1() throws Exception {
        Conf x1 = Conf.create(
                UtCnv.toMap(
                        "a", 1,
                        "b", UtCnv.toMap(
                                "a1", 1,
                                "b1", 2
                        )
                )
        );

        Conf x2 = x1.cloneConf();

        // изменяем оригинал
        x1.setValue("z", 1);
        x1.getConf("b").setValue("z1", 1);

        conf.printConf(x1);
        conf.printConf(x2);

        assertEquals(conf.toMap(x2).toString(), "{a=1, b={a1=1, b1=2}}");
    }

    @Test
    public void uniqueKey1() throws Exception {
        Conf x1 = Conf.create();
        x1.setValue("#", 0);
        x1.setValue("#", 1);
        x1.remove("#0");
        x1.setValue("#", 2);
        conf.printConf(x1);
        assertEquals(conf.toMap(x1).toString(), "{#1=1, #1#1=2}");
    }

    @Test
    public void setValuePath1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a", 0);
        x.setValue("a/b/c", 1);
        x.setValue("a/b/d", 11);
        x.setValue("a/b/d", null);
        x.setValue("b", 2);

        assertEquals(x.getString("b"), "2");
        assertEquals(x.getString("a/b/c"), "1");

        conf.printConf(x);
        assertEquals(conf.toMap(x).toString(), "{a={b={c=1}}, b=2}");
    }

    @Test
    public void named1() throws Exception {
        Conf x = Conf.create();
        x.setValue("a/b/x", 1);
        x.setValue("a/c/x", 11);

        Conf x1;
        x1 = x.getConf("a");
        assertEquals(x1.getName(), "a");
        x1 = x.getConf("a/b");
        assertEquals(x1.getName(), "b");

        x1 = x.getConf("a");
        List<String> z = new ArrayList<>();
        for (Object f : x.getConf("a").values()) {
            z.add(((Conf) f).getName());
        }
        assertEquals(z.toString(), "[b, c]");

    }

}
