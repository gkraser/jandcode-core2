package jandcode.xcore;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.xcore.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Beans_Test extends App_Test {

    public static class Bean1 extends BaseComp {

        private String attr1 = "--";
        private Conf conf;

        protected void onConfigure(BeanConfig cfg) throws Exception {
            super.onConfigure(cfg);
            //
            this.conf = cfg.getConf();
        }

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        public Conf getConf() {
            return conf;
        }
    }

    public static class Base1 extends BaseComp {
    }

    public static class Real1 extends Base1 {
    }

    public static class Default1 extends Base1 {
    }


    @Test
    public void test1() throws Exception {
        Bean1 a = (Bean1) app.bean("bean1");
        assertEquals(a.getName(), "bean1");
        assertEquals(a.getAttr1(), "111");
        assertEquals(a.getConf().getName(), "bean1");
        assertEquals(a.getConf().getValue("attr1"), "111");
        System.out.println(UtConf.save(a.getConf()).toString());
    }

    @Test
    public void test_castForDefault() throws Exception {
        Conf x = app.getConf().getConf("castForDefault/a1");

        Base1 z = app.create(x, Default1.class);
        assertEquals(z.getClass(), Default1.class);

        x = app.getConf().getConf("castForDefault/a2");
        z = app.create(x, Default1.class);
        assertEquals(z.getClass(), Real1.class);
    }

    @Test
    public void test_prototype() throws Exception {
        assertTrue(app.create("bean2.prototype") instanceof Bean1);

        try {
            app.bean("bean2.prototype");
            fail();
        } catch (Exception e) {
            //ignore
        }

    }


}
