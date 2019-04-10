package jandcode.mdoc.source;

import jandcode.commons.*;
import jandcode.commons.named.*;

import java.util.*;

/**
 * Дерево по списку исходных файлов
 */
public class TreeNodeSourceFile implements INamed {

    private String name;
    private boolean folder;
    private SourceFile sourceFile;
    private Map<String, TreeNodeSourceFile> childs = new LinkedHashMap<>();

    //////

    public TreeNodeSourceFile(Collection<SourceFile> items) {
        this("ROOT");
        buildTree(items);
    }

    //////

    private TreeNodeSourceFile(String name) {
        this.name = name;
        this.folder = true;
    }

    private TreeNodeSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
        this.name = UtFile.filename(sourceFile.getPath());
    }

    /**
     * Создать дерево по списку исходных файлов
     *
     * @param items
     */
    private void buildTree(Collection<SourceFile> items) {
        for (SourceFile f : items) {
            addFile(f);
        }
    }

    private void addFile(SourceFile f) {
        String ar[] = f.getPath().split("/");
        TreeNodeSourceFile cur = this;
        for (int i = 0; i < ar.length - 1; i++) {
            String folder = ar[i];
            TreeNodeSourceFile it = cur.childs.get(folder);
            if (it == null) {
                it = new TreeNodeSourceFile(folder);
                cur.childs.put(folder, it);
            }
            cur = it;
        }
        cur.childs.put(ar[ar.length - 1], new TreeNodeSourceFile(f));
    }

    //////

    /**
     * Имя файла или папки
     */
    public String getName() {
        return name;
    }

    /**
     * true - папка
     */
    public boolean isFolder() {
        return folder;
    }

    /**
     * Ссылка на файл
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }

    /**
     * Дочерние в папке
     */
    public Collection<TreeNodeSourceFile> getChilds() {
        return childs.values();
    }

}
