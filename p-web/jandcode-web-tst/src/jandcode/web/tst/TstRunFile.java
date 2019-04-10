package jandcode.web.tst;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.virtfile.*;

import java.util.*;

/**
 * Информация о запускаемых файлах для _tst/run
 */
public class TstRunFile extends BaseComp implements ITstResFilter {

    private List<RunFileInfo> runFiles = new ArrayList<>();

    class RunFileInfo {
        Conf conf;
        String mask;
        String template;
        String templateDefault;

        public RunFileInfo(Conf conf) {
            this.conf = conf;
            this.mask = conf.getString("mask");
            this.template = conf.getString("template");
            this.templateDefault = conf.getString("templateDefault");
            if (UtString.empty(this.templateDefault) && !UtString.empty(this.template)) {
                this.templateDefault = this.template;
            }
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        for (Conf conf : getApp().getConf().getConfs("web/tst/run-file")) {
            runFiles.add(new RunFileInfo(conf));
        }
    }

    public boolean isIgnore(VirtFile f) {
        if (f.isPrivate()) {
            return true;
        }
        if (f.isFolder()) {
            List<VirtFile> files = getApp().bean(WebService.class).findFiles(f.getPath());
            for (VirtFile f1 : files) {
                if (!isIgnore(f1)) {
                    return false;
                }
            }
            return true;
        }
        String p = f.getPath();
        boolean flag = false;
        for (RunFileInfo m : runFiles) {
            if (UtVDir.matchPath(m.mask, p)) {
                flag = true;
                break;
            }
        }
        return !flag;
    }

    public String findTemplate(String path) {
        for (RunFileInfo m : runFiles) {
            if (UtVDir.matchPath(m.mask, path)) {
                return m.template;
            }
        }
        return "";
    }

    public String findTemplateDefault(String path) {
        for (RunFileInfo m : runFiles) {
            if (UtVDir.matchPath(m.mask, path)) {
                return m.templateDefault;
            }
        }
        return "";
    }

}
