package jandcode.commons.conf;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConfExpander_Test extends CustomConf_Test {

    @Test
    public void field1() throws Exception {
        Conf x = Conf.create();
        UtConf.load(x).fromFile(utils.getTestFile("data/expander1.cfx"));
        conf.printConf(x);

        utils.delim();
        //
        ConfExpander exp = UtConf.createExpander(x);
        Conf x1 = exp.expand("field", "memo");
        conf.printConf(x1);
        assertEquals(conf.toMap(x1).toString(),
                "{sys=1, string=1, in-string=from-memo, memo=1}");
    }

    @Test
    public void domain1() throws Exception {
        Conf x = Conf.create();
        UtConf.load(x).fromFile(utils.getTestFile("data/expander1.cfx"));
        conf.printConf(x);

        utils.delim();
        //
        ConfExpander exp = UtConf.createExpander(x);
        exp.addRuleContainer("domain", "field", "field");
        exp.addRuleObject("domain", "ref", "field");
        //
        Conf x1 = exp.expand("domain", "abonent");
        conf.printConf(x1);
        assertEquals(conf.toMap(x1).toString(), "" +
                "{sys=1, id=1, in-id=from-abonent, ref={sys=1, string=1, " +
                "in-string=from-memo, memo=1, from-abonent=1}, field={id={sys=1, " +
                "string=1, in-string=1}, f1={sys=1, string=1, in-string=from-memo, " +
                "memo=1, abonent-f1=1}, f2={sys=1, string=1, in-string=from-memo, " +
                "memo=from-abonent, abonent-f2=1}}, abonent=1}");
    }

    @Test
    public void notInherited1() throws Exception {
        Conf x = Conf.create();
        UtConf.load(x).fromFile(utils.getTestFile("data/expander2-not-inherited.cfx"));
        conf.printConf(x);

        //
        utils.delim();
        //
        ConfExpander exp = UtConf.createExpander(x);
        exp.addRuleNotInherited("abs");
        exp.addRuleNotInherited("field", "abs-f");
        exp.addRuleNotInherited("tag.*");
        exp.addRuleNotInherited("field", "tag-f.*");

        Conf x1 = exp.expand("field", "memo");
        conf.printConf(x1);
        assertEquals(conf.toMap(x1).toString(),
                "{string=1, memo=1}");

        x1 = exp.expand("field", "string");
        conf.printConf(x1);
        assertEquals(conf.toMap(x1).toString(),
                "{string=1, abs=true, abs-f=true, tag.db=true, tag-f.db=true}");

    }


}
