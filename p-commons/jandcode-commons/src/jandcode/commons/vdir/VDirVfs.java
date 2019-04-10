package jandcode.commons.vdir;

import jandcode.commons.vdir.impl.*;

/**
 * Реализация виртуального каталога через VFS
 */
public class VDirVfs extends CustomVDir {

    public VDirVfs() {
        this.fileSystem = new VfsVFileSystem();
    }

}
