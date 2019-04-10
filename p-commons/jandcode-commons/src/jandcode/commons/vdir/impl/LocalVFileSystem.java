package jandcode.commons.vdir.impl;

import jandcode.commons.*;

import java.io.*;

public class LocalVFileSystem extends CustomVFileSystem {

    public String abs(String path) {
        return UtFile.abs(path);
    }

    public Object resolveFile(String rootPath, String relativePath) {
        String fn = UtFile.join(rootPath, relativePath);
        return new File(fn);
    }

    public boolean exists(Object file) throws Exception {
        return ((File) file).exists();
    }

    public boolean isDir(Object file) throws Exception {
        return ((File) file).isDirectory();
    }

    public Object[] listChilds(Object file) throws Exception {
        return ((File) file).listFiles();
    }

    public String fileName(Object file) throws Exception {
        return ((File) file).getName();
    }

    public String fullName(Object file) throws Exception {
        return ((File) file).getAbsolutePath();
    }

    public String relativePath(String fromPath, String toPath) throws Exception {
        return UtFile.getRelativePath(fromPath, toPath);
    }
}
