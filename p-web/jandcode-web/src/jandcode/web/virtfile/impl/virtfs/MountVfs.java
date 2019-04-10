package jandcode.web.virtfile.impl.virtfs;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.web.virtfile.*;
import jandcode.web.virtfile.impl.*;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.FileType;

import java.util.*;

/**
 * Точка монтирования для каталога vfs
 */
public class MountVfs extends VirtFSMount {

    private FileObject root;

    // могут ли папки в этой точке монтирования изменяться
    private boolean canChange;

    class FolderContentVFS implements FolderContent {
        // виртуальный путь папки
        private String path;

        // реальная папка
        private FileObject realFolder;

        // true - путь вообще не из этой точки монтирования
        private boolean badPath;

        // если не null, имеем только одну эту виртуальную папку
        private String oneFolder;

        // файлы в папке
        private List<VirtFile> files;

        // существует ли такая папка в реальной жизни
        private boolean exists;

        // сохраненное время модификации папки при загрузке
        private long lastModSaved;

        public FolderContentVFS(String path) {
            this.path = path;

            String localPath = "";

            if (UtString.empty(getVirtualPath())) {
                localPath = path;
            } else {
                PrefixVsPath p = new PrefixVsPath(getVirtualPath(), path);
                if (p.getRel() == PrefixVsPath.REL_NO) {
                    badPath = true;

                } else if (p.getRel() == PrefixVsPath.REL_EQUAL) {
                    localPath = "";

                } else if (p.getRel() == PrefixVsPath.REL_PATH_EMPTY) {
                    oneFolder = p.getVirtualPath();

                } else if (p.getRel() == PrefixVsPath.REL_PREFIX_STARTSWITH_PATH) {
                    oneFolder = p.getVirtualPath2();

                } else if (p.getRel() == PrefixVsPath.REL_PATH_STARTSWITH_PREFIX) {
                    localPath = p.getVirtualPath();

                } else {
                    badPath = true;

                }
            }

            try {
                doLoad(localPath);
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }

        }

        protected void doLoad(String localPath) throws Exception {
            files = new ArrayList<>();
            if (badPath) {
                // ничего не делаем
                return;
            }

            if (oneFolder != null) {
                // одна папка
                files.add(new VirtFileFolder(UtVDir.joinNormalized(path, oneFolder)));
            } else {
                realFolder = getRoot().resolveFile(localPath);
                if (!realFolder.exists() || realFolder.getType() != FileType.FOLDER) {
                    exists = false;
                } else {
                    lastModSaved = realFolder.getContent().getLastModifiedTime();
                    exists = true;

                    // если виртуальный путь содержит WEB-INF или META-INF -
                    // это приватные файлы
                    String tmpPath = "/" + path + "/";
                    boolean privateFile = tmpPath.contains("/WEB-INF/") ||
                            tmpPath.contains("/META-INF/");
                    //

                    Map<String, VirtFile> tmp = new HashMap<>();
                    for (FileObject f1 : realFolder.getChildren()) {
                        String bn = f1.getName().getBaseName();
                        String fn = UtVDir.joinNormalized(path, bn);
                        if (f1.getType() == FileType.FOLDER) {
                            tmp.put(bn, new VirtFileFolder(fn, true, privateFile ||
                                    bn.equals("WEB-INF") || bn.equals("META-INF")));
                        } else {
                            String ext = UtFile.ext(bn);
                            VirtFile wf = VirtFileVFS.create(f1, path, MountVfs.this,
                                    privateFile ||
                                            ext.equals("java") ||
                                            ext.equals("groovy") ||
                                            ext.equals("class"));
                            bn = wf.getName();
                            if (tmp.containsKey(bn)) {
                                throw new XError("Файл {0} определен в каталоге {1} несколько раз", bn, realFolder);
                            }
                            tmp.put(bn, wf);
                        }
                    }
                    files.addAll(tmp.values());
                }

            }
        }

        public boolean isValid() {
            return !badPath;
        }

        public boolean isExists() {
            return isValid() && (oneFolder != null || exists);
        }

        public List<VirtFile> getFiles() {
            return files;
        }

        public String getPath() {
            return path;
        }

        public boolean isCanChange() {
            return canChange && isValid() && oneFolder == null && realFolder != null;
        }

        public boolean isChange() {
            if (!isCanChange()) {
                return false;
            }
            try {
                long t1 = realFolder.getContent().getLastModifiedTime();
                return t1 != lastModSaved;
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }

    }

    //////

    public MountVfs() {
    }

    public MountVfs(String virtualPath, String realPath) {
        setVirtualPath(virtualPath);
        setRealPath(realPath);
    }

    protected FileObject getRoot() {
        if (root == null) {
            throw new XError("root not assigned");
        }
        return root;
    }

    public String getRealPath() {
        return getRoot().toString();
    }

    public void setRealPath(String path) {
        root = UtFile.getFileObject(path);
        // флаг - может ли содержимое папок изменятся
        canChange = true;
        String s = root.toString();
        if (s.startsWith("jar:") || s.startsWith("zip:")) {
            canChange = false;
        }
    }

    public FolderContent getFolderContent(String path) {
        return new FolderContentVFS(path);
    }

    public void validate() {
        if (root == null) {
            throw new XError("Не указано значение для realPath у mount [{0}]", getName());
        }
    }
}
