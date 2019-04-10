package jandcode.commons.test;

import jandcode.commons.*;
import jandcode.commons.error.*;

import java.io.*;

/**
 * Построитель каталогов для тестов
 */
public class DirTestBuilder {

    private String base;

    /**
     * Создать экземпляр
     *
     * @param base базовый каталог внутри каталога с тестами
     */
    public DirTestBuilder(String base) {
        this.base = UtFile.join(UtFile.getWorkdir(), "temp/DirTestBuilder", base);
    }

    /**
     * Очиcтить каталог
     */
    public void clean() {
        UtFile.cleanDir(base);
    }

    /**
     * Полный путь по относительному
     */
    public String path(String relPath) {
        return UtFile.join(base, UtVDir.normalize(relPath));
    }

    /**
     * Создать каталог.
     * Возвращает полный путь созданного каталога.
     */
    public String mkdir(String relPath) {
        String s = path(relPath);
        UtFile.mkdirs(s);
        return s;
    }

    /**
     * Создать файл.
     * Возвращает полный путь созданного файла.
     */
    public String mkfile(String relPath, String content) {
        String s = path(relPath);
        UtFile.cleanFile(s);
        try {
            UtFile.saveString(content, new File(s));
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
        return s;
    }

}
