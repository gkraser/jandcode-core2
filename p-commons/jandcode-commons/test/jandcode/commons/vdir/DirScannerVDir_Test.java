package jandcode.commons.vdir;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class DirScannerVDir_Test extends Utils_Test {

    void printList(List<VFile> itms) {
        for (VFile it : itms) {
            System.out.println(it.getVirtualPath() + " => " + it.getRealPath());
        }
    }

    void printList1(List<VFile> itms) {
        for (VFile it : itms) {
            System.out.println(it.getVirtualPath());
        }
    }

    @Test
    public void test2() throws Exception {
        VDir vdir = new VDirLocal();
        vdir.addRoot(UtFile.abs(utils.getTestPath() + "/.."));

        DirScannerVDir sc = new DirScannerVDir(vdir);
        sc.setDir("**/*.cfx");
        List<VFile> itms = sc.load();

        printList(itms);
    }

    @Test
    public void test3() throws Exception {
        VDir vdir = new VDirLocal();
        vdir.addRoot(UtFile.abs(utils.getTestPath() + "/.."));

        DirScannerVDir sc = new DirScannerVDir(vdir);
        sc.setDir("sim*/**/*.*");
        List<VFile> itms = sc.load();

        printList1(itms);
    }


}
