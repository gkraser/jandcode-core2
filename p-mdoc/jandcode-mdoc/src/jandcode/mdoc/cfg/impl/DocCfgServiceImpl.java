package jandcode.mdoc.cfg.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.mdoc.cfg.*;

public class DocCfgServiceImpl extends BaseComp implements DocCfgService {

    public FilesetCfg createFilesetCfg(Conf conf) {
        FilesetCfg fs = new FilesetCfg();
        UtReflect.getUtils().setProps(fs, conf);
        return fs;
    }

    public DocCfg createDocCfg(Conf conf) {
        DocCfg cfg = new DocCfg();
        for (Conf x : conf.getConfs("prop")) {
            cfg.setProp(x.getName(), x.getValue("value"));
        }
        for (Conf x : conf.getConfs("src")) {
            FilesetCfg fs = createFilesetCfg(x);
            cfg.addSrc(fs);
        }
        return cfg;
    }

}
