package jandcode.web.virtfile.impl.virtfs;

import jandcode.commons.*;
import jandcode.web.virtfile.*;
import jandcode.web.virtfile.impl.*;

import java.util.*;

/**
 * Виртуальная файловая система
 */
public class VirtFS implements ITmlCheck {

    private List<VirtFSMount> mounts = new ArrayList<>();
    private ITmlCheck tmlCheck;
    private Map<String, CachedFolder> cacheFolders = new HashMap<>();

    public List<VirtFSMount> getMounts() {
        return mounts;
    }

    public void addMount(VirtFSMount p) {
        p.validate();
        p.setTmlCheck(this);
        mounts.add(p);
        // кеш чистим сразу же
        clearCache();
    }

    public void setTmlCheck(ITmlCheck tmlCheck) {
        this.tmlCheck = tmlCheck;
    }

    public boolean isTml(String ext) {
        if (tmlCheck == null) {
            return false;
        }
        return tmlCheck.isTml(ext);
    }

    ////// внешний интерфейс

    public List<VirtFile> findFiles(String path) {
        path = UtVDir.normalize(path);
        CachedFolder f = getCachedFolder(path);
        if (f == null) {
            return new ArrayList<>();
        }
        return f.getFiles();
    }

    public VirtFile findFile(String path) {
        path = UtVDir.normalize(path);
        String pt = UtFile.path(path);
        String fn = UtFile.filename(path);
        CachedFolder f = getCachedFolder(pt);
        if (f == null) {
            return null;
        }
        return f.getFile(fn);
    }

    //////

    class CachedFolder {

        private String path;
        private Map<String, VirtFile> files = new HashMap<>();
        private List<FolderContent> contents = new ArrayList<>();
        private boolean exists;
        private boolean canChange;

        public CachedFolder(String path) {
            this.path = path;
            doLoad();
        }

        private void doLoad() {
            for (int i = mounts.size() - 1; i >= 0; i--) {
                VirtFSMount p = mounts.get(i);
                FolderContent c = p.getFolderContent(path);
                contents.add(c);
            }
            for (FolderContent c : contents) {
                if (!c.isExists()) {
                    continue;
                }
                exists = true;
                if (c.isCanChange()) {
                    canChange = true;
                }
                List<VirtFile> lst = c.getFiles();
                for (VirtFile f : lst) {
                    if (!files.containsKey(f.getName())) {
                        files.put(f.getName(), f);
                    }
                }
            }
        }

        private List<VirtFile> getFiles() {
            List<VirtFile> res = new ArrayList<>();
            res.addAll(files.values());
            return res;
        }

        private VirtFile getFile(String name) {
            return files.get(name);
        }

        private boolean isExists() {
            return exists;
        }

        private boolean isCanChange() {
            return canChange;
        }

        private boolean isChange() {
            if (!isCanChange()) {
                return false;
            }
            for (FolderContent c : contents) {
                if (c.isChange()) {
                    return true;
                }
            }
            return false;
        }
    }

    //////

    private CachedFolder getCachedFolder(String path) {
        CachedFolder res = cacheFolders.get(path);
        if (res == null) {
            synchronized (this) {
                res = cacheFolders.get(path);
                if (res == null) {
                    res = new CachedFolder(path);
                    if (res.isExists()) {
                        cacheFolders.put(path, res);
                    } else {
                        res = null;
                    }
                }
            }
        }
        return res;
    }

    /**
     * Очистить кеш. Возвращает true, если кеш очищен, false - если и так был пустой.
     */
    public boolean clearCache() {
        synchronized (this) {
            if (cacheFolders.size() > 0) {
                cacheFolders = new HashMap<>();
                return true;
            }
        }
        return false;
    }
}
