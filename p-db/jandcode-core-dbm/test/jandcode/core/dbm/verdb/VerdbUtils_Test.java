package jandcode.core.dbm.verdb;

import jandcode.commons.test.*;
import jandcode.core.dbm.verdb.impl.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class VerdbUtils_Test extends Utils_Test {

    @Test
    public void extractVersionFromFilename() throws Exception {
        long v;

        v = VerdbUtils.extractVersionFromFilename(null);
        assertEquals(v, -1);

        v = VerdbUtils.extractVersionFromFilename("");
        assertEquals(v, -1);

        v = VerdbUtils.extractVersionFromFilename("aaa");
        assertEquals(v, -1);

        v = VerdbUtils.extractVersionFromFilename("0");
        assertEquals(v, 0);

        v = VerdbUtils.extractVersionFromFilename("0000");
        assertEquals(v, 0);

        v = VerdbUtils.extractVersionFromFilename("0001");
        assertEquals(v, 1);

        v = VerdbUtils.extractVersionFromFilename("0000-xxx-yyy.z");
        assertEquals(v, 0);

        v = VerdbUtils.extractVersionFromFilename("0001-xxx-yyy.z");
        assertEquals(v, 1);

        v = VerdbUtils.extractVersionFromFilename("8888888888888888888888888888888888888888888888888888888888888888888888-xxx-yyy.z");
        assertEquals(v, -1);

        v = VerdbUtils.extractVersionFromFilename("0001.txt");
        assertEquals(v, 1);
    }

}
