package jandcode.mdoc.builder;

import jandcode.commons.*;

import java.util.*;

/**
 * Хранилище выводимых файлов
 */
public class OutFileHolder {

    private Map<String, OutFile> items = new LinkedHashMap<>();
    private Map<String, OutFile> itemsBySourcePath = new LinkedHashMap<>();
    private Map<String, OutFile> itemsByTopicId = new LinkedHashMap<>();

    public void clear() {
        this.items.clear();
        this.itemsBySourcePath.clear();
        this.itemsByTopicId.clear();
    }

    /**
     * Список всех выходных файлов
     */
    public Collection<OutFile> getItems() {
        return items.values();
    }

    public void add(OutFile f) {
        this.items.put(f.getPath(), f);
        this.itemsBySourcePath.put(f.getSourceFile().getPath(), f);
        if (f.getTopic() != null) {
            this.itemsByTopicId.put(f.getTopic().getId(), f);
        }
    }

    public OutFile findByPath(String path) {
        path = UtVDir.normalize(path);
        return this.items.get(path);
    }

    public OutFile findBySourcePath(String path) {
        path = UtVDir.normalize(path);
        return this.itemsBySourcePath.get(path);
    }

    public OutFile findByTopicId(String id) {
        return this.itemsByTopicId.get(id);
    }

    public void remove(OutFile f) {
        if (f == null) {
            return;
        }
        this.items.remove(f.getPath());
        this.itemsBySourcePath.remove(f.getSourceFile().getPath());
        if (f.getTopic() != null) {
            this.itemsByTopicId.remove(f.getTopic().getId());
        }
    }

    /**
     * Переместить файл
     *
     * @param f      какой файл
     * @param toPath куда (destPath)
     */
    public void move(OutFile f, String toPath) {
        OutFile f1 = this.items.get(f.getPath());
        if (f1 == null) {
            return;
        }
        remove(f1);
        f1.setPath(toPath);
        add(f1);
    }

}
