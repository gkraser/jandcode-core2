package jandcode.xcore.std;

import jandcode.xcore.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataDir_Test extends App_Test {

    DataDirService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(DataDirService.class);
    }

    @Test
    public void test_notExists() throws Exception {
        try {
            svc.getPath("_not_exists_");
            fail();
        } catch (Exception e) {
            //
        }
    }

    @Test
    public void test1() throws Exception {
        String s;
        s = svc.getPath("root");
        System.out.println(s);
        s = svc.getPath("temp");
        System.out.println(s);
        s = svc.getPath("temp.workdir");
        System.out.println(s);
        s = svc.getPath("one");
        System.out.println(s);
    }

    @Test
    public void test1_localPath() throws Exception {
        String s;
        s = svc.getPath("temp", "t1/t2");
        System.out.println(s);
        s = svc.getPath("temp.workdir", "t1/t2");
        System.out.println(s);
    }


}
