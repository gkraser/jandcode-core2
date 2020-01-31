package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.jc.*

/**
 * Выявление физического файла по логическому имени в контексте проекта
 */
class FileResolver extends ProjectScript {

    //////

    /**
     * Поиск файла или каталога. Файл ищется в следующих местах (в порядке приоритета):
     * <ul>
     * <li>относительно каталога проекта</li>
     * <li>относительно каталогов jc-data всех доступных проектов</li>
     * </ul>
     *
     * @return реальное полное имя файла или null, если не найден
     */
    String findFile(String path) {
        String f;

        f = wd(path);
        if (UtFile.exists(f)) {
            return f;
        }

        f = getCtx().service(JcDataService).findFile(path);
        if (f != null) {
            return f;
        }

        return null;
    }

    /**
     * см. {@link FileResolver#findFile(java.lang.String)}
     *
     * @return реальное полное имя файла или ошибка, если не найден
     */
    String getFile(String path) {
        String f = findFile(path);
        if (f == null) {
            throw new XError("Файл не найден: {0}", path);
        }
        return f;
    }

}
