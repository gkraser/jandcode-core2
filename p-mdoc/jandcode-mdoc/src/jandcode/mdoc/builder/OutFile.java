package jandcode.mdoc.builder;

import jandcode.commons.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

import java.util.*;

/**
 * Выводимый файл
 */
public class OutFile {

    private String path;
    private SourceFile sourceFile;
    private boolean need;
    private Topic topic;
    private Set<String> dependFiles = new HashSet<>();

    public OutFile(String path, SourceFile sourceFile, Topic topic) {
        this.setPath(path);
        this.sourceFile = sourceFile;
        this.topic = topic;
        //
        addDependFile(this.sourceFile);
        if (this.topic != null) {
            addDependFile(this.topic.getSourceFile());
        }
    }

    /**
     * Путь файла в каталоге назначения
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = UtVDir.normalize(path);
    }

    /**
     * Файл, который нужно будет вывести
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }

    /**
     * Статья, для которой этот файл
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * true - файл нужен в выводе
     */
    public boolean isNeed() {
        return need;
    }

    public void setNeed(boolean need) {
        this.need = need;
    }

    //////

    /**
     * Пути файлов, от которых зависит содержимое этого файла.
     */
    public Set<String> getDependFiles() {
        return dependFiles;
    }

    /**
     * Добавить файл в список зависимостей
     */
    public void addDependFile(SourceFile f) {
        if (f == null) {
            return;
        }
        this.dependFiles.add(f.getPath());
    }

    /**
     * Добавить файл в список зависимостей
     */
    public void addDependFile(String path) {
        if (path == null) {
            return;
        }
        path = UtVDir.normalize(path);
        if (!UtString.empty(path)) {
            this.dependFiles.add(path);
        }
    }

}
