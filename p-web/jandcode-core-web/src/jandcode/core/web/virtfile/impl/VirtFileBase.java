package jandcode.core.web.virtfile.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.core.web.virtfile.*;
import org.apache.commons.vfs2.*;

import java.io.*;

/**
 * Базовый предок для виртуальных файлов
 */
public abstract class VirtFileBase implements VirtFile {

    private String name;
    private String path;
    private boolean folder;
    private boolean exist;

    public VirtFileBase(String path, boolean folder, boolean exist) {
        this.folder = folder;
        this.exist = exist;
        this.path = path;
        if (path.indexOf('/') != -1) {
            name = UtFile.filename(path);
        } else {
            name = path;
        }
    }

    public String getName() {
        return name;
    }

    public String getFolderPath() {
        return UtFile.path(path);
    }

    public boolean isFolder() {
        return folder;
    }

    public boolean isFile() {
        return !isFolder();
    }

    public String getPath() {
        return path;
    }

    public boolean isExists() {
        return exist;
    }

    public String toString() {
        return getPath();
    }

    public InputStream getInputStream() {
        throw new XError("Unsupport getInputStream for {0}", getPath());
    }

    public long getLastModTime() {
        return 0;
    }

    public boolean isPrivate() {
        return false;
    }

    public long getSize() {
        return 0;
    }

    public String getRealPath() {
        return "";
    }

    public FileObject getFileObject() {
        return null;
    }

    public String loadText() {
        StringLoader ldr = new StringLoader();
        try {
            ldr.load().fromStream(getInputStream());
        } catch (Exception e) {
            throw new XErrorMark(e, "virtfile: " + getPath());
        }
        return ldr.getResult();
    }

}
