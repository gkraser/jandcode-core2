package jandcode.mdoc.source.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.vdir.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.mdoc.cfg.*;
import jandcode.mdoc.source.*;

import java.util.*;

/**
 * Загрузка из fileset в контексте приложения.
 * dir может принимать форму: jc-data:PATH
 */
public class FilesetSourceFileLoader implements SourceFileLoader {

    public static final String JC_DATA_PREFIX = "jc-data:";

    private App app;
    private FilesetCfg fileset;
    private String prefix;

    /**
     * @param app     контект приложения
     * @param fileset набор файлов
     * @param prefix  дополнительный префикс
     */
    public FilesetSourceFileLoader(App app, FilesetCfg fileset, String prefix) {
        this.app = app;
        this.fileset = fileset;
        this.prefix = prefix;
    }

    public List<SourceFile> loadFiles() throws Exception {
        if (UtString.empty(fileset.getDir())) {
            throw new XError("Не указан dir в fileset");
        }
        VDirSourceFileLoader loader;
        String jcDataPath = UtString.removePrefix(fileset.getDir(), JC_DATA_PREFIX);
        if (jcDataPath == null) {
            VDir vdir = new VDirVfs();
            vdir.addRoot(fileset.getDir());
            loader = new VDirSourceFileLoader(
                    vdir,
                    "",
                    UtVDir.join(this.prefix, fileset.getPrefix()),
                    fileset.getIncludes(),
                    fileset.getExcludes()
            );
        } else {
            loader = new VDirSourceFileLoader(
                    app.bean(JcDataAppService.class).getVdir(),
                    jcDataPath,
                    UtVDir.join(this.prefix, fileset.getPrefix()),
                    fileset.getIncludes(),
                    fileset.getExcludes()
            );
        }
        return loader.loadFiles();
    }

}
