package jandcode.core.web.virtfile.impl;


/**
 * Универсальная папка
 */
public class VirtFileFolder extends VirtFileBase {

    private boolean privateFolder;

    public VirtFileFolder(String path) {
        super(path, true, true);
    }

    public VirtFileFolder(String path, boolean exist) {
        super(path, true, exist);
    }

    public VirtFileFolder(String path, boolean exist, boolean privateFolder) {
        super(path, true, exist);
        this.privateFolder = privateFolder;
    }

    public boolean isTmlBased() {
        return false;
    }

    public String getContentFileType() {
        return "folder";
    }

    public String getFileType() {
        return "folder";
    }

    public boolean isPrivate() {
        return privateFolder;
    }

}
