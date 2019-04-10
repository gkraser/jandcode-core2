package jandcode.web.virtfile.impl.virtfs;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.web.virtfile.impl.*;
import org.apache.commons.vfs2.*;

import java.io.*;

/**
 * Обычный файл VFS
 */
public class VirtFileVFS extends VirtFileBase {

    private FileObject fileObject;
    private String tmlType;
    private boolean _private;

    /**
     * Создать экземпляр файла по правилам шаблон/не шаблон
     *
     * @param parentFolderPath нормализованный виртуальный путь папки, в которой находится файл
     */
    public static VirtFileVFS create(FileObject fileObject, String parentFolderPath, ITmlCheck tmlCheck, boolean isPrivate) {
        String bn = fileObject.getName().getBaseName();
        String ext1 = UtFile.ext(bn);
        String fn2 = UtFile.removeExt(bn);
        String ext2 = UtFile.ext(fn2);

        if (tmlCheck != null && !UtString.empty(ext2) && tmlCheck.isTml(ext1)) {
            // это шаблон
            return new VirtFileVFS(fileObject, UtVDir.joinNormalized(parentFolderPath, fn2), ext1, isPrivate);
        } else {
            // обычный файл
            return new VirtFileVFS(fileObject, UtVDir.joinNormalized(parentFolderPath, bn), null, isPrivate);
        }
    }

    public VirtFileVFS(FileObject fileObject, String path, String tmlType, boolean isPrivate) {
        super(path, false, true);
        this.fileObject = fileObject;
        this.tmlType = tmlType;
        this._private = isPrivate;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    //////

    public boolean isTmlBased() {
        return tmlType != null;
    }

    public String getFileType() {
        if (tmlType == null) {
            return getContentFileType();
        }
        return tmlType;
    }

    public String getContentFileType() {
        return UtFile.ext(getName());
    }

    //////

    public boolean isExists() {
        try {
            return fileObject.exists();
        } catch (Exception e) {
            return false;
        }
    }

    public long getLastModTime() {
        try {
            return fileObject.getContent().getLastModifiedTime();
        } catch (FileSystemException e) {
            return 0;
        }
    }

    public InputStream getInputStream() {
        try {
            return fileObject.getContent().getInputStream();
        } catch (FileSystemException e) {
            throw new XErrorWrap(e);
        }
    }

    public boolean isPrivate() {
        return _private;
    }

    public long getSize() {
        try {
            return fileObject.getContent().getSize();
        } catch (FileSystemException e) {
            return 0;
        }
    }

    public String getRealPath() {
        return fileObject.toString();
    }

}
