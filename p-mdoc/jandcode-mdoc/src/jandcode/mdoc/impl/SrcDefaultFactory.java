package jandcode.mdoc.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.vdir.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.mdoc.cfg.*;

import java.util.*;

public class SrcDefaultFactory {

    /**
     * Загрузка всех fileset, указанных в mdoc/src-default
     */
    public List<FilesetCfg> loadSrcDefaultFilesets(App app, Conf srcDefaultConf) {
        DocCfgService svcDocCfg = app.bean(DocCfgService.class);

        List<FilesetCfg> res = new ArrayList<>();
        for (Conf src1 : srcDefaultConf.getConfs()) {
            String dir = src1.getString("dir");
            if (UtString.empty("dir")) {
                continue;
            }
            String jcDataPath = UtString.removePrefix(dir, "jc-data:");
            if (jcDataPath != null) {
                List<VFile> vfLst = app.bean(JcDataAppService.class).getVdir().findFiles(jcDataPath);
                for (VFile vf : vfLst) {
                    FilesetCfg fs1 = svcDocCfg.createFilesetCfg(src1);
                    fs1.setDir(vf.getRealPath());
                    res.add(fs1);
                }
            } else {
                FilesetCfg fs1 = svcDocCfg.createFilesetCfg(src1);
                res.add(fs1);
            }
        }

        return res;
    }

}
