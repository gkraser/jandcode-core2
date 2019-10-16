package jandcode.core.web.gsp.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.virtfile.*;
import org.apache.commons.vfs2.*;

import java.util.*;

/**
 * Поставщик gsp из описаний в conf
 */
public class GspProviderConf extends BaseComp implements IGspProvider {

    public List<GspDef> loadGsps() throws Exception {
        List<GspDef> res = new ArrayList<>();
        Collection<Conf> lst = getApp().getConf().getConfs("web/gsp");
        for (Conf x : lst) {
            String nm = UtConf.getNameAsPath(x);
            String cn = x.getString("class");
            String pt = x.getString("path");
            GspDef g;
            if (!UtString.empty(cn)) {
                g = new GspDefCls(getApp(), UtClass.getClass(cn));
            } else if (!UtString.empty(pt)) {
                g = resolveGsp(pt);
            } else {
                throw new XError("Для gsp [{0}] нужно указать атрибут class или path", nm);
            }
            g.setName(nm);
            res.add(g);
        }
        return res;
    }

    /**
     * gspName рассматривается как имя файла VFS или VirtFile.
     */
    public GspDef resolveGsp(String gspName) {
        boolean abs = UtFile.isAbsolute(gspName);

        GspDef g;

        if (abs) {
            FileObject f = UtFile.getFileObject(gspName);

            try {
                if (!f.exists()) {
                    throw new XError("File {0} not exists", gspName);
                }
            } catch (FileSystemException e) {
                throw new XErrorWrap(e);
            }

            g = new GspDefFileObject(getApp(), f);
            g.setName(f.toString());

        } else {
            VirtFile f = getApp().bean(WebService.class).findFile(gspName);
            if (f == null || !f.isExists()) {
                throw new XError("File {0} not exists", gspName);
            }

            g = new GspDefVirtFile(getApp(), f);
            g.setName(f.getPath());
        }

        return g;

    }
}
