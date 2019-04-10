package jandcode.commons.vdir;

import jandcode.commons.error.*;

import java.util.*;

/**
 * Реализация IVDirWrap по умолчанию
 */
public class VDirWrap implements IVDirWrap {

    protected VDir vdir;

    public VDirWrap(VDir vdir) {
        this.vdir = vdir;
    }

    public VDir getVdir() {
        return vdir;
    }

    public String findFile(String path) {
        VFile z = vdir.findFile(path);
        if (z == null) {
            return null;
        }
        return z.getRealPath();
    }

    public String getFile(String path) {
        String f = findFile(path);
        if (f == null) {
            throw new XError("Не найден файл [{0}]", path);
        }
        return f;
    }

    public List<String> findFiles(String path) {
        List<VFile> z = vdir.findFiles(path);
        List<String> res = new ArrayList<>();
        for (VFile item : z) {
            res.add(item.getRealPath());
        }
        return res;
    }

}
