package jandcode.commons.io.impl;

import jandcode.commons.*;
import org.apache.commons.vfs2.*;

import java.util.*;

public class DirScannerVfs extends BaseDirScanner<FileObject> {
    protected FileObject createItem(String dir) {
        return UtFile.getFileObject(dir);
    }

    protected boolean isFile(FileObject f) {
        try {
            return f.getType() == FileType.FILE;
        } catch (FileSystemException e) {
            return false;
        }
    }

    protected List<FileObject> listDir(FileObject f) {
        FileObject[] ar = null;
        try {
            ar = f.getChildren();
        } catch (FileSystemException e) {
            return null;
        }
        if (ar != null) {
            List<FileObject> lst = Arrays.asList(ar);
            lst.sort((a, b) -> a.getName().getBaseName().compareToIgnoreCase(b.getName().getBaseName()));
            return lst;
        } else {
            return null;
        }

    }

    protected String getName(FileObject f) {
        return f.getName().getBaseName();
    }
}
