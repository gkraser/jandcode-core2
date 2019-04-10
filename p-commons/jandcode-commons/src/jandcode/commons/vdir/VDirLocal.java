package jandcode.commons.vdir;

import jandcode.commons.vdir.impl.*;

/**
 * Реализация виртуального каталога через локальную файловую систему
 */
public class VDirLocal extends CustomVDir {

    public VDirLocal() {
        this.fileSystem = new LocalVFileSystem();
    }

}
