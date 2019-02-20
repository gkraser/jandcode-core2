package jandcode.commons.vdir;

import jandcode.commons.io.impl.*;

import java.util.*;

/**
 * Сканер для виртуального каталога
 */
public class DirScannerVDir extends BaseDirScanner<VFile> {

    private VDir vdir;

    public DirScannerVDir(VDir vdir) {
        this.vdir = vdir;
    }

    protected VFile createItem(String dir) {
        return vdir.findFile(dir);
    }

    protected boolean isFile(VFile f) {
        return f.isFile();
    }

    protected List<VFile> listDir(VFile f) {
        return vdir.findFiles(f.getVirtualPath());
    }

    protected String getName(VFile f) {
        return f.getFileName();
    }

}
