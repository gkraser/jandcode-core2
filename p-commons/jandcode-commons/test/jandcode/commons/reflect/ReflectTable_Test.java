package jandcode.commons.reflect;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectTable_Test extends Utils_Test {

    static class Table1 {
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }

    static class Table2 extends Table1 {
        private String text;
        private String byName;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @FieldProps(dict = "dict1")
        public String getByName() {
            return byName;
        }
    }

    static class Table3 extends Table2 {
        @FieldProps(dict = "dict2")
        private String dict2;

        public String getDict2() {
            return dict2;
        }

        public void setDict2(String dict2) {
            this.dict2 = dict2;
        }
    }

    @Test
    public void test1() throws Exception {
        ReflectTable t = UtReflect.getReflectTable(Table2.class);
        for (var f : t.getFields()) {
            System.out.println("" + f.getName() + ":" + f.getPropNames() + ":" + f.getGetter());
        }
        assertEquals(t.getFields().size(), 3);
        assertEquals(t.findField("ID").getName(), "id");
        assertEquals(t.findField("TExt").getName(), "text");
        assertEquals(t.findField("byName").getName(), "byName");
    }

    @Test
    public void dict1() throws Exception {
        ReflectTable t = UtReflect.getReflectTable(Table3.class);
        assertEquals(t.findField("byName").getProp("dict"), "dict1");
        assertEquals(t.findField("dict2").getProp("dict"), "dict2");
    }


}
