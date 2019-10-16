package jandcode.core.web.tst;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.web.virtfile.*;

import java.util.*;

/**
 * Фильтр для файлов, которые не должны показыватся в _tst
 */
public class TstIgnoreFile extends BaseComp implements ITstResFilter {

    private List<String> ignoreFiles = new ArrayList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        for (Conf ignf : getApp().getConf().getConfs("web/tst/ignore-file")) {
            String mask = ignf.getString("mask");
            if (!UtString.empty(mask)) {
                this.ignoreFiles.add(mask);
            }
        }
    }

    public boolean isIgnore(VirtFile f) {
        if (f.isPrivate()) {
            return true;
        }
        String p = f.getPath();
        for (String m : ignoreFiles) {
            if (UtVDir.matchPath(m, p)) {
                return true;
            }
        }
        return false;
    }

}
