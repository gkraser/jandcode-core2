package jandcode.core.web.gsp.impl;

import jandcode.core.*;
import org.apache.commons.vfs2.*;

/**
 * gsp объявленная как FileObject
 */
public class GspDefFileObject extends CustomGspDefFileObject {

    private FileObject file;

    public GspDefFileObject(App app, FileObject file) {
        setApp(app);
        this.file = file;
    }

    protected FileObject getFileObject() {
        return file;
    }

    public String getGspPath() {
        return file.toString();
    }

}
