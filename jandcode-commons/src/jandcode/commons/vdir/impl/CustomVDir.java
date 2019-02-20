package jandcode.commons.vdir.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.vdir.*;

import java.util.*;

public abstract class CustomVDir implements VDir {

    protected List<CustomVRoot> rootPaths = new ArrayList<>();
    protected CustomVFileSystem fileSystem;

    //////

    public void addRoot(String path) {
        addRoot(path, "");
    }

    public void addRoot(String path, String prefixPath) {
        prefixPath = UtVDir.normalize(prefixPath);
        path = fileSystem.abs(path);
        for (CustomVRoot r : rootPaths) {
            if (r.isSame(path, prefixPath)) {
                return; // уже есть такой
            }
        }
        CustomVRoot r = new CustomVRoot();
        r.setRootPath(path);
        r.setPrefix(prefixPath);
        rootPaths.add(0, r);
    }

    public List<VRoot> getRoots() {
        return (List) rootPaths;
    }

    public List<VFile> findFiles(String path0) {
        int index = 0;
        path0 = UtVDir.normalize(path0);
        List<VFile> res = new ArrayList<>();
        HashSet<String> used = new HashSet<>();
        HashSet<String> dummyDirs = new HashSet<>();
        try {
            for (CustomVRoot root : rootPaths) {
                if (path0.length() == 0) {
                    String rootPrefixFolder = root.getRootPrefixFolder();
                    if (rootPrefixFolder != null) {
                        dummyDirs.add(rootPrefixFolder);
                    }
                }
                String path1 = root.prepareVPath(path0);
                if (path1 == null) {
                    String dd = root.getDummyFolder(path0);
                    if (dd != null) {
                        dummyDirs.add(dd);
                    }
                    continue;
                }
                Object f = fileSystem.resolveFile(root.getRootPath(), path1);
                if (fileSystem.exists(f) && fileSystem.isDir(f)) {
                    Object[] lst = fileSystem.listChilds(f);
                    for (Object f1 : lst) {
                        String nm = fileSystem.fileName(f1);
                        if (!used.contains(nm)) {
                            used.add(nm);
                            res.add(createItem(UtVDir.join(path0, nm), fileSystem.fullName(f1), fileSystem.isDir(f1), index));
                        }
                    }
                }
                index++;
            }
            if (dummyDirs.size() > 0) {
                for (String s : dummyDirs) {
                    if (!used.contains(s)) {
                        res.add(createItem(UtVDir.join(path0, s), "DUMMY", true, index));
                        index++;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public VFile findFile(String path0) {
        int index = 0;
        try {
            path0 = UtVDir.normalize(path0);
            for (CustomVRoot root : rootPaths) {
                String path1 = root.prepareVPath(path0);
                if (path1 == null) {
                    continue;
                }
                Object fn = fileSystem.resolveFile(root.getRootPath(), path1);
                if (fileSystem.exists(fn)) {
                    return createItem(path0, fileSystem.fullName(fn), fileSystem.isDir(fn), index);
                }
                index++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> getRealPathList(String path0) {
        List<String> res = new ArrayList<>();
        try {
            path0 = UtVDir.normalize(path0);
            for (CustomVRoot root : rootPaths) {
                String path1 = root.prepareVPath(path0);
                if (path1 == null) {
                    continue;
                }
                Object fn = fileSystem.resolveFile(root.getRootPath(), path1);
                if (fileSystem.exists(fn)) {
                    res.add(fileSystem.fullName(fn));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public String getVirtualPath(String absolutePath) {
        try {
            for (CustomVRoot root : rootPaths) {
                String p = fileSystem.relativePath(root.getRootPath(), absolutePath);
                if (p != null && p.indexOf("..") == -1) {
                    if (!root.isEmpty()) {
                        p = root.getPrefix() + "/" + p;
                    }
                    return UtVDir.normalize(p);
                }
            }
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
        return null;
    }

    public List<VFile> getFileEntry(String virtualPath) {
        ArrayList<VFile> res = new ArrayList<>();
        int index = 0;
        try {
            virtualPath = UtVDir.normalize(virtualPath);
            for (CustomVRoot root : rootPaths) {
                String path1 = root.prepareVPath(virtualPath);
                if (path1 == null) {
                    continue;
                }
                Object fn = fileSystem.resolveFile(root.getRootPath(), path1);
                if (fileSystem.exists(fn)) {
                    res.add(createItem(virtualPath, fileSystem.fullName(fn), fileSystem.isDir(fn), index));
                }
                index++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    protected VFile createItem(String vpath, String rpath, boolean dir, int index) {
        return new VFileImpl(this, vpath, rpath, dir, index);
    }

}
