package jandcode.core.web.virtfile.impl.filetype;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.*;

import java.util.*;

/**
 * Хранилище для типов файлов
 */
public class FileTypeHolder extends BaseComp implements ITmlCheck {

    private NamedList<FileType> items = new DefaultNamedList<>("filetype [{0}] not found");

    public void add(Collection<Conf> lst) {
        for (Conf x : lst) {
            add(x);
        }
    }

    protected void add(Conf conf) {
        FileType ft = getApp().create(conf, FileTypeImpl.class);
        items.add(ft);
    }

    public FileType find(String name) {
        FileType ft = items.find(name);
        if (ft == null) {
            ft = items.find("private");
        }
        return ft;
    }

    public List<FileType> getItems() {
        return items;
    }

    public boolean isTml(String ext) {
        FileType ft = find(ext);
        return ft.isTml();
    }

}
