package jandcode.mdoc.source.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.vdir.*;
import jandcode.mdoc.source.*;
import org.apache.commons.vfs2.*;

import java.util.*;

/**
 * Загрузчик из виртуального каталога.
 * Универсальная штука. Например обычный каталог можно представить как
 * виртуальный с одним root.
 */
public class VDirSourceFileLoader implements SourceFileLoader {

    private VDir vdir;
    private String dir;
    private String prefix;
    private List<String> includes;
    private List<String> excludes;

    public VDirSourceFileLoader(VDir vdir, String dir, String prefix, List<String> includes, List<String> excludes) {
        this.vdir = vdir;
        this.dir = dir;
        this.prefix = prefix;
        this.includes = includes;
        this.excludes = excludes;
        //
        if (UtString.empty(this.prefix)) {
            this.prefix = null;
        }
        if (UtString.empty(this.dir)) {
            this.dir = "";
        }
    }

    public List<SourceFile> loadFiles() throws Exception {

        for (VRoot root : this.vdir.getRoots()) {
            FileObject f = UtFile.getFileObject(root.getRootPath());
            if (!f.exists()) {
                throw new XError("Не найден путь: " + f.toString());
            }
        }

        List<SourceFile> res = new ArrayList<>();
        DirScanner<VFile> ps = new DirScannerVDir(this.vdir);
        if (this.includes != null) {
            for (String p : this.includes) {
                ps.include(p);
            }
        }
        if (this.excludes != null) {
            for (String p : this.excludes) {
                ps.exclude(p);
            }
        }
        List<VFile> lst = ps.load();
        for (VFile f1 : lst) {
            String path = UtVDir.getRelPath(this.dir, f1.getVirtualPath());
            if (this.prefix != null) {
                path = UtVDir.join(prefix, path);
            }
            SourceFile f = new VfsSourceFile(path, f1.getRealPath());
            res.add(f);
        }
        return res;
    }

}
