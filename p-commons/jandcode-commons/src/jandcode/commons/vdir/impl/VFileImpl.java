package jandcode.commons.vdir.impl;

import jandcode.commons.*;
import jandcode.commons.vdir.*;

import java.util.*;

public class VFileImpl implements VFile {

    private VDir own;
    private String vpath;
    private String rpath;
    private boolean dir;
    private int index;

    public VFileImpl(VDir own, String vpath, String rpath, boolean dir, int index) {
        this.own = own;
        this.vpath = vpath;
        this.rpath = rpath;
        this.dir = dir;
        this.index = index;
    }

    public String getVirtualPath() {
        return vpath;
    }

    public String getRealPath() {
        return this.rpath;
    }

    public List<String> getRealPathList() {
        return own.getRealPathList(vpath);
    }

    public boolean isDir() {
        return dir;
    }

    public boolean isFile() {
        return !isDir();
    }

    public int getIndex() {
        return index;
    }

    public String getFileName() {
        return UtFile.filename(getVirtualPath());
    }

    public String getExt() {
        return UtFile.ext(getVirtualPath());
    }

    public String toString() {
        return getRealPath();
    }

}
