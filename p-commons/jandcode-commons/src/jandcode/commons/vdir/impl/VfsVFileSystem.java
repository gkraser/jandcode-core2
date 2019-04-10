package jandcode.commons.vdir.impl;

import jandcode.commons.*;
import org.apache.commons.vfs2.*;

public class VfsVFileSystem extends CustomVFileSystem {

    public String abs(String path) {
        FileObject fo = UtFile.getFileObject(path);
        return fo.toString();
    }

    public Object resolveFile(String rootPath, String relativePath) {
        String fn = UtFile.join(rootPath, relativePath);
        return UtFile.getFileObject(fn);
    }

    public boolean exists(Object file) throws Exception {
        return ((FileObject) file).exists();
    }

    public boolean isDir(Object file) throws Exception {
        return ((FileObject) file).getType() == FileType.FOLDER;
    }

    public Object[] listChilds(Object file) throws Exception {
        return ((FileObject) file).getChildren();
    }

    public String fileName(Object file) throws Exception {
        return ((FileObject) file).getName().getBaseName();
    }

    public String fullName(Object file) throws Exception {
        return ((FileObject) file).toString();
    }

    public String relativePath(String fromPath, String toPath) throws Exception {
        FileObject to = UtFile.getFileObject(toPath);
        FileObject from = UtFile.getFileObject(fromPath);
        return from.getName().getRelativeName(to.getName());
    }

}
