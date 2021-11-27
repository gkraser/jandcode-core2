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

        public String getByName() {
            return byName;
        }
    }

    @Test
    public void test1() throws Exception {
        ReflectTable t = UtReflect.getReflectTable(Table2.class);
        for (var f : t.getFields()) {
            System.out.println("" + f.getName() + ":" + f.getGetter());
        }
        assertEquals(t.getFields().size(), 3);
        assertEquals(t.findField("ID").getName(), "id");
        assertEquals(t.findField("TExt").getName(), "text");
        assertEquals(t.findField("byName").getName(), "byName");
    }


}
