package jandcode.core;

import jandcode.core.test.*;
import org.apache.commons.vfs2.*;
import org.junit.jupiter.api.*;

public class UtApp_Test extends App_Test {

    @Test
    public void getFileObject_1() throws Exception {
        FileObject fo;
        fo = UtApp.getFileObject(app, "a/b.txt");
        System.out.println(fo);

        fo = UtApp.getFileObject(app, "jc-data:a/b.txt");
        System.out.println(fo);
    }


}
