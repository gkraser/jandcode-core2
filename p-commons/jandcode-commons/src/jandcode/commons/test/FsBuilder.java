package jandcode.commons.test;

import jandcode.commons.*;
import jandcode.commons.error.*;

import java.io.*;
import java.util.*;

/**
 * Построитель файловой системы для тестов.
 * Пример использования:
 * <code><pre>{@code
 * FsBuilder.FsFile f;
 * FsBuilder b = new FsBuilder("temp/fs-1");
 * b.clean();
 * b.build(
 *   b.dir("d3"),
 *   b.dir("d4"),
 *      f = b.file("f1"),
 *      b.dir("dd1",
 *         b.file("f1")
 *      )
 *   ),
 *   b.dir("d2")
 * );
 * <p>
 * }</pre></code>
 */
public class FsBuilder {

    private String path;

    public class FsFile {
        protected String name;
        protected String path;

        public FsFile(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public void bindPath(String destPath) {
            this.path = UtFile.abs(UtFile.join(destPath + "/" + name));
        }

        public void create(String destPath) throws Exception {
            bindPath(destPath);
            UtFile.saveString("1", new File(destPath + "/" + name));
        }
    }

    public class FsDir extends FsFile {
        protected List<FsFile> files = new ArrayList<>();

        public FsDir(String name) {
            super(name);
        }

        public void create(String destPath) throws Exception {
            bindPath(destPath);
            UtFile.mkdirs(path);
            for (FsFile f : files) {
                f.create(path);
            }
        }
    }


    /**
     * Создать
     *
     * @param path базовый путь, где будем создавать
     */
    public FsBuilder(String path) {
        if (UtString.empty(path)) {
            throw new XError("empty path");
        }
        this.path = UtFile.abs(path);
    }

    /**
     * Базовый абсолютный путь
     */
    public String getPath() {
        return path;
    }

    /**
     * Путь до localPath внутри базового пути
     */
    public String path(String localPath) {
        return UtFile.join(path, localPath);
    }

    /**
     * Очистить базовый каталог
     */
    public void clean() {
        UtFile.cleanDir(path);
    }

    /**
     * Построить
     */
    public FsBuilder build(FsFile... files) throws Exception {
        for (FsFile f : files) {
            f.create(path);
        }
        return this;
    }

    /**
     * Описание каталога
     */
    public FsDir dir(String name, FsFile... files) {
        FsDir d = new FsDir(name);
        Collections.addAll(d.files, files);
        return d;
    }

    /**
     * Описание каталога. Синоним для dir.
     */
    public FsDir d(String name, FsFile... files) {
        return dir(name, files);
    }

    /**
     * Описание файла
     */
    public FsFile file(String name) {
        return new FsFile(name);
    }

    /**
     * Описание файла. Синоним для file.
     */
    public FsFile f(String name) {
        return file(name);
    }

}
