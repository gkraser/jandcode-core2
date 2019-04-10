package jandcode.commons.test;

import jandcode.commons.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class OutMapSaver_Test extends Utils_Test {

    private Map makeMap1() {
        Map m = UtCnv.toMap(
                "a", 1,
                "ddd", 2,
                "m1", UtCnv.toMap(
                        "a", 1,
                        "b", 2
                ),
                "c", Arrays.asList(1, 2, 3)
        );
        return m;
    }

    @Test
    public void test1() throws Exception {
        utils.outMap(makeMap1());
    }

    @Test
    public void test2() throws Exception {
        utils.outMap(makeMap1(), true);
    }

}
