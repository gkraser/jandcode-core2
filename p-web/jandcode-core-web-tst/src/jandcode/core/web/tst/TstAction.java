package jandcode.core.web.tst;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.web.action.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.virtfile.*;

public class TstAction extends BaseAction {

    public void index() throws Exception {
        getReq().render(new GspTemplate("jandcode/core/web/tst/showpath.gsp", UtCnv.toMap(
                "path", getReq().getString("path")
        )));
    }

    public void run() throws Exception {
        String path = getReq().getString("path");

        if (!UtString.empty(path)) {
            VirtFile f = getReq().getWebService().findFile(path);
            TstRunFile rf = getApp().create(TstRunFile.class);
            if (f != null && f.isFile() && !rf.isIgnore(f)) {
                String template = rf.findTemplate(f.getPath());
                if (UtString.empty(template)) {
                    getReq().redirect(f.getPath());
                } else {
                    VirtFile tf = findTemplate(f, template);
                    if (tf == null) {
                        String templateDefault = rf.findTemplateDefault(f.getPath());
                        if (!UtString.empty(templateDefault)) {
                            tf = getReq().getWebService().findFile(templateDefault);
                        }
                        if (tf == null) {
                            throw new XError("Не найден шаблон [{0}] для файла [{1}]", template, f.getPath());
                        }
                    }
                    getReq().render(new GspTemplate(tf.getPath(), UtCnv.toMap(
                            "path", f.getPath()
                    )));

                }
                return;
            }
        }

        getReq().render(new GspTemplate("jandcode/core/web/tst/showpath-run.gsp", UtCnv.toMap(
                "path", path
        )));
    }

    private VirtFile findTemplate(VirtFile file, String template) {
        String cp = file.getFolderPath();
        while (!UtString.empty(cp)) {
            String fn = UtVDir.join(cp, template);
            VirtFile ft = getReq().getWebService().findFile(fn);
            if (ft != null) {
                return ft;
            }
            cp = UtFile.path(cp);
        }
        return null;
    }

    /**
     * Рендер произвольного gsp
     */
    public void render() throws Exception {
        String path = getReq().getActionMethodPathInfo();
        String ext = UtFile.ext(path);
        if (UtString.empty(ext)) {
            path = path + ".gsp";
        }
        getReq().renderGsp(path);
    }

}
