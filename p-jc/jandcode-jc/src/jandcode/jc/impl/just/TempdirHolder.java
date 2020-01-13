package jandcode.jc.impl.just;

import jandcode.commons.*;
import jandcode.jc.*;

public class TempdirHolder implements ITempdir {

    private String baseTempPath;

    public TempdirHolder() {
        String vr = UtVersion.getVersion("jandcode.jc");
        String wd = UtFile.getWorkdir();
        String nm = UtFile.filename(wd);
        String cv = System.getProperty("java.class.version");
        if (UtString.empty(nm)) {
            nm = "root";
        }
        nm = nm + "--" + UtString.md5Str(vr + "|" + wd + "|" + cv);
        baseTempPath = UtFile.join(getTempdirRoot(), "wd", nm);
    }

    /**
     * Базовый каталог для временных файлов jc
     */
    public String getTempdirRoot() {
        return UtFile.join(UtFile.getTempdir(), JcConsts.TEMP_DIR);
    }

    public String getTempdir(String localPath) {
        return UtFile.join(baseTempPath, localPath);
    }

    public String getTempdirCommon(String localPath) {
        return UtFile.join(getTempdirRoot(), localPath);
    }

}
