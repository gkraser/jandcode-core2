package jandcode.core.web.virtfile.impl.virtfs;

import jandcode.commons.io.impl.*;
import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.*;

import java.util.*;

public class DirScannerVirtFS extends BaseDirScanner<VirtFile> {

    private VirtFS fs;

    public DirScannerVirtFS(VirtFS fs, String dir) {
        this.fs = fs;
        this.setDir(dir);
    }

    protected VirtFile createItem(String dir) {
        VirtFile res = fs.findFile(dir);
        if (res == null) {
            res = new VirtFileFolder(dir, false);
        }
        return res;
    }

    protected boolean isFile(VirtFile f) {
        return f.isFile();
    }

    protected List<VirtFile> listDir(VirtFile f) {
        List<VirtFile> lst = fs.findFiles(f.getPath());
        lst.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return lst;
    }

    protected String getName(VirtFile f) {
        return f.getName();
    }

}
