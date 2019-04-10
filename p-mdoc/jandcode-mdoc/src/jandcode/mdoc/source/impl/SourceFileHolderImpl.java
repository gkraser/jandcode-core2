package jandcode.mdoc.source.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.mdoc.source.*;

import java.util.*;

public class SourceFileHolderImpl implements SourceFileHolder {

    private Map<String, SourceFile> items = new LinkedHashMap<>();

    public void add(SourceFile f) {
        items.put(f.getPath(), f);
    }

    public Collection<SourceFile> getItems() {
        return items.values();
    }

    public Collection<SourceFile> findFiles(String mask) {
        Collection<SourceFile> res = new ArrayList<>();
        for (SourceFile f : this.items.values()) {
            if (UtVDir.matchPath(mask, f.getPath())) {
                res.add(f);
            }
        }
        return res;
    }

    public SourceFile find(String virtualPath) {
        virtualPath = UtVDir.normalize(virtualPath);
        return items.get(virtualPath);
    }

    public SourceFile get(String virtualPath) {
        SourceFile a = find(virtualPath);
        if (a == null) {
            throw new XError("Не найден файл {0}", virtualPath);
        }
        return a;
    }

    public Iterator<SourceFile> iterator() {
        return getItems().iterator();
    }

}
