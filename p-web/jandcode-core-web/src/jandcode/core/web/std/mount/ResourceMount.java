package jandcode.core.web.std.mount;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.web.virtfile.*;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.*;
import org.slf4j.*;

import java.net.*;
import java.util.*;

/**
 * Поставщик mount из ресурсов в classpath.
 * В атрибуте resourcePath указываем путь ресурсов.
 * В атрибуте virtualPath указываем куда монтируем.
 * Все найденные ресурсы примонтируются по указанному пути.
 */
public class ResourceMount extends BaseMount implements IMountProvider {

    protected static Logger log = LoggerFactory.getLogger(ResourceMount.class);

    private String resourcePath;
    private boolean autoVirtualPath;

    /**
     * Монтируемый ресурс
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Если true, то в виртуальный путь включаются все пустые промежуточные папки
     */
    public void setAutoVirtualPath(boolean autoVirtualPath) {
        this.autoVirtualPath = autoVirtualPath;
    }

    //////

    public List<Mount> loadMounts() throws Exception {

        if (UtString.empty(resourcePath)) {
            throw new XError("resourcePath не установлен для mount [{0}]", getName());
        }

        List<Mount> res = new ArrayList<>();

        int n = 0;
        Enumeration<URL> en = getClass().getClassLoader().getResources(resourcePath);
        while (en.hasMoreElements()) {
            URL r = en.nextElement();
            FileObject f = UtFile.getFileObject(r.toString());

            String vp = getVirtualPath();
            if (autoVirtualPath) {
                FileObject f1 = findNotEmptyFolder(f);
                vp = UtVDir.join(vp, f.getName().getRelativeName(f1.getName()));
                f = f1;
            }
            log.debug("found mount resource: " + f + ", virtualPath: " + vp);

            n++;
            Mount m = createMountVfs(
                    getName() + "--" + n + "--" + f.getName().getBaseName(),
                    vp,
                    f.toString()
            );
            res.add(m);

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
