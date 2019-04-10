package jandcode.mdoc.source.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import org.apache.commons.vfs2.*;

import java.io.*;

public class VfsSourceFile extends BaseSourceFile {

    private static FileSelector ALLFILES = new AllFileSelector();

    private FileObject fileObject;

    public VfsSourceFile(String path, String realPath) {
        this.fileObject = UtFile.getFileObject(realPath);
        setPath(path);
    }

    public String getRealPath() {
        return fileObject.toString();
    }

    public void copyTo(String destFile) throws Exception {
        FileObject dest = UtFile.getFileObject(destFile);
        dest.copyFrom(this.fileObject, ALLFILES);
    }

    public void copyTo(OutputStream stm) throws Exception {
        InputStream inps = this.fileObject.getContent().getInputStream();
        try {
            UtFile.copyStream(inps, stm);
        } finally {
            inps.close();
        }
    }

    public String getText() {
        try {
            return UtFile.loadString(this.fileObject.toString());
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public long getLastModTime() {
        try {
            return this.fileObject.getContent().getLastModifiedTime();
        } catch (FileSystemException e) {
            return 0;
        }
    }

}
