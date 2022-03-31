package jandcode.core;

import jandcode.commons.*;
import jandcode.commons.vdir.*;
import jandcode.core.std.*;
import org.apache.commons.vfs2.*;

/**
 * Статические утилиты для приложения
 */
public class UtApp {

    /**
     * Получить FileObject из имени файла. Если имя не полное - возвращается относительно
     * рабочего каталога.
     * <p>
     * Особенность метода: обрабатывается префикс 'jc-data:'. При его наличии
     * Поиск файла ведется через сервис {@link JcDataAppService}.
     *
     * @param filename имя файла. Может быть полной строкой в формате VFS
     */
    public static FileObject getFileObject(App app, String filename) {
        if (!UtString.empty(filename)) {
            if (filename.startsWith("jc-data:")) {
                filename = filename.substring(8);
                JcDataAppService svc = app.bean(JcDataAppService.class);
                VFile vf = svc.getVdir().findFile(filename);
                if (vf != null) {
                    filename = vf.getRealPath();
                }
            }
        }
        return UtFile.getFileObject(filename);
    }


}
