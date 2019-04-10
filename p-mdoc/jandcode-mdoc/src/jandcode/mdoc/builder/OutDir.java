package jandcode.mdoc.builder;

import jandcode.commons.*;

/**
 * Каталог для генерации
 */
public class OutDir {

    private String dir;

    public OutDir(String dir) {
        this.dir = UtFile.abs(dir);
    }

    public String getDir() {
        return dir;
    }

    /**
     * Вывести файл
     */
    public void outFile(OutFile f) throws Exception {
        String destFile = UtFile.join(getDir(), f.getPath());
        UtFile.cleanFile(destFile);
        f.getSourceFile().copyTo(destFile);
    }

}
