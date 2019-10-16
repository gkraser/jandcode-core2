package jandcode.core.web.file;

import jandcode.commons.io.*;
import jandcode.core.test.*;
import jandcode.core.web.*;
import jandcode.core.web.virtfile.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

public class VirtFileService_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        WebService svc = app.bean(WebService.class);
        List<Mount> z = svc.getMounts();
        for (Mount x : z) {
            System.out.println(x.getName() + "=[" + x.getVirtualPath() + "]=" + x.getRealPath());
        }
        VirtFile f = svc.findFile("a/b/c/f1.txt");
        System.out.println(f.getName());
        System.out.println(f.getPath());
        //
        StringLoader ldr = new StringLoader();
        InputStream strm = f.getInputStream();
        ldr.load().fromStream(strm);
        System.out.println(ldr.getResult());

    }

    @Test
    public void test_fileTypes() throws Exception {
        WebService svc = app.bean(WebService.class);
        for (FileType ft : svc.getFileTypes()) {
            System.out.println(ft.getName() + " - " + ft.getMime() + " - " + ft.getType());
        }
    }


}
