package jandcode.core.dbm.verdb;

import jandcode.core.dbm.test.*;
import jandcode.core.dbm.verdb.impl.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class VerdbDirLoader_Test extends Dbm_Test {

    private String dirListToStr(List<VerdbDir> lst) {
        StringBuilder sb = new StringBuilder();
        for (VerdbDir dir : lst) {
            sb.append("|").append(dir.getVersion().getText()).append("|");
            for (VerdbFile file : dir.getFiles()) {
                sb.append(">").append(file.getVersion().getText());
            }
        }
        return sb.toString();
    }

    @Test
    public void test1() throws Exception {
        String path = utils.getTestFile("data/dir1");
        VerdbDirLoader ldr = new VerdbDirLoader();
        List<VerdbDir> lst = ldr.loadDir(path);
        String s = dirListToStr(lst);
        assertEquals(s, "|1.0.0|>1.1.0>1.3.0|2.0.0|>2.2.0>2.3.0>2.4.0|3.0.0|>3.2.0");
        assertSame(lst.get(0), lst.get(0).getFiles().get(0).getDir());
    }

}
