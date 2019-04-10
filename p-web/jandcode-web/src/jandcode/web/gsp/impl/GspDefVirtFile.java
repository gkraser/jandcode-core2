package jandcode.web.gsp.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.web.virtfile.*;
import org.apache.commons.vfs2.*;

/**
 * gsp объявленная как FileObject
 */
public class GspDefVirtFile extends CustomGspDefFileObject {

    private VirtFile file;

    public GspDefVirtFile(App app, VirtFile file) {
        setApp(app);
        this.file = file;
    }

    protected FileObject getFileObject() {
        FileObject fo = file.getFileObject();
        if (fo == null) {
            throw new XError("Виртуальный Файл [{0}] не связан с реальным файлом", file);
        }
        return fo;
    }

    public String getGspPath() {
        return file.getPath();
    }

}
