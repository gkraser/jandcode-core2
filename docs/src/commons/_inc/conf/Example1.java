package commons._inc.conf;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class Example1 {

    @Test
    public void create1() throws Exception {
        //= create1
        // создание пустого объекта
        Conf x = UtConf.create();

        // создание из Map
        Map m = new HashMap();
        m.put("a", 1);
        m.put("b", 2);
        Conf x1 = UtConf.create();

        //=

    }


}
