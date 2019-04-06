package commons._inc.conf;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.test.*;
import jandcode.mdoc.cm.*;
import jandcode.mdoc.source.*;

import java.util.*;

public class UseConf1 extends BaseCodeGen {

    private StringBuilder prn(Conf x, String v) {
        StringBuilder sb = new StringBuilder();
        if (v != null) {
            sb.append(v).append(" = \n");
        }
        sb.append(new OutMapSaver(x).save().toString());
        return sb;
    }

    private StringBuilder prn(Conf x) {
        return prn(x, null);
    }

    private Conf loadConf(String fn) throws Exception {
        SourceFile sf = getSourceFile(fn);
        getOutFile().addDependFile(sf);
        Conf x = UtConf.create();
        UtConf.load(x).fromString(sf.getText());
        return x;
    }

    //////

    public void create_empty() throws Exception {
        Conf x = UtConf.create();
    }

    public void create_fromMap() throws Exception {
        Map m = new LinkedHashMap();
        m.put("a", 1);
        m.put("b", 2);
        Conf x = UtConf.create(m);
        outText(prn(x));
    }

    public void create_fromList() throws Exception {
        List lst = new ArrayList();
        lst.add(1);
        lst.add("q1");
        lst.add(new HashMap<>());
        lst.add(Arrays.asList(1, 2));
        Conf x = UtConf.create(lst);
        outText(prn(x));
    }

    public void set_simple() throws Exception {
        Conf x = UtConf.create();
        x.setValue("a", 1);
        x.put("b", 2);
        outText(prn(x));
    }

    public void get_simple() throws Exception {
        Conf x = loadConf("UseConf1-data1.cfx");
        //= body
        outText(prn(x, "x"));
        String s = x.getString("str1");
        outText(s);
        int i = x.getInt("int1");
        outText(i);
        //=
    }

    public void path1() throws Exception {
        Conf x = UtConf.create();
        x.setValue("a/b/c", 1);
        x.setValue("a/b/d", 2);
        outText(prn(x));
        String s = x.getString("a/b/c");
        outText(s);
    }

    public void value_conf1() throws Exception {
        Conf x = UtConf.create();
        x.setValue("a", UtConf.create());
        outText(prn(x));
    }

    public void value_conf2() throws Exception {
        Conf x = UtConf.create();
        x.setValue("a", UtConf.create());
        Conf x1 = x.getConf("a");
        x1.setValue("b", 2);
        outText(prn(x, "x"));
        outText(prn(x1, "x1"));
        x1 = x.findConf("AAA");
        outText("null");
    }

    public void value_conf3_find() throws Exception {
        Conf x = UtConf.create();
        Conf x1 = x.findConf("a/b/c", true);
        outText(prn(x, "x"));
    }

    public void value_conf3_map_and_list() throws Exception {
        Conf x = UtConf.create();

        Map m = new LinkedHashMap();
        m.put("a", 1);
        m.put("b", 2);
        m.put("c", Arrays.asList("i1", "i2"));
        x.setValue("map1", m);

        x.setValue("list1", Arrays.asList("item1", "item2"));

        outText(prn(x, "x"));
    }

    public void noname1() throws Exception {
        Conf x = UtConf.create();
        x.setValue("#", 0);
        x.setValue("#", 1);
        outText(prn(x, "x"));
    }

    public void load1() throws Exception {
        Conf x1 = UtConf.create();
        UtConf.load(x1).fromString("{\"a\":1}", "a.json");
        outText(prn(x1, "x"));
        Conf x2 = UtConf.create();
        UtConf.load(x2).fromString("<root><b>2</b></root>", "a.cfx");
        outText(prn(x2, "x"));
        Conf x3 = UtConf.create();
        UtConf.load(x3).fromString("<root><a z=\"3\">a</a><b>2</b></root>", "a.xml");
        outText(prn(x3, "x"));
    }

    public void join1() throws Exception {
        Conf x = UtConf.create();
        x.setValue("a/b", 1);

        Conf x1 = UtConf.create();
        x1.setValue("a/c", 2);

        x.join(x1);
        // объект x1 независимый
        x1.setValue("z", 1);

        outText(prn(x, "x"));
        outText(prn(x1, "x1"));
    }

}
