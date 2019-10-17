package jandcode.core.web.std.mount;

import jandcode.commons.*;
import jandcode.core.std.*;
import jandcode.core.web.virtfile.*;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.FileType;
import org.slf4j.*;

import java.util.*;

/**
 * Поставщик mount для папок jc-data/webroot
 */
public class JcDataMount extends BaseMount implements IMountProvider {

    protected static Logger log = LoggerFactory.getLogger(JcDataMount.class);

    public List<Mount> loadMounts() throws Exception {
        List<Mount> res = new ArrayList<>();

        JcDataAppService jcdataSvc = getApp().bean(JcDataAppService.class);
        List<String> webroots = jcdataSvc.getVdir().getRealPathList("webroot");
        int cnt = webroots.size();
        for (String webroot : webroots) {
            FileObject f = UtFile.getFileObject(webroot);

            String vp = getVirtualPath();
            FileObject f1 = findNotEmptyFolder(f);
            vp = UtVDir.join(vp, f.getName().getRelativeName(f1.getName()));
            f = f1;

            log.debug("found mount source: " + f + ", virtualPath: " + vp);

            // добавляем в обратном порядке!
            res.add(0, createMountVfs(
                    getName() + "--" + cnt,
                    vp,
                    f.toString()
            ));
            cnt--;
        }

        return res;
    }

    private FileObject findNotEmptyFolder(FileObject f) throws Exception {
        FileObject cur = f;
        while (true) {
            FileObject[] ch = cur.getChildren();
            if (ch.length == 1 && ch[0].getType() == FileType.FOLDER) {
                cur = ch[0];
                continue;
            }
            break;
        }
        return cur;
    }

}
