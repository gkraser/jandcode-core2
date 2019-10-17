package jandcode.core.web.virtfile.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.core.web.virtfile.FileType;
import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.filetype.*;
import jandcode.core.web.virtfile.impl.virtfs.*;
import org.apache.commons.vfs2.*;

import java.util.*;

public class VirtFileServiceImpl extends BaseComp implements VirtFileService {

    protected VirtFS virtFS;
    protected FileTypeHolder fileTypeHolder;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // filetype
        fileTypeHolder = getApp().create(FileTypeHolder.class);
        fileTypeHolder.add(getApp().getConf().getConfs("web/filetype"));

        // virtfs
        virtFS = new VirtFS();
        virtFS.setTmlCheck(fileTypeHolder);

        // mount
        List<Conf> z = UtConf.sortByWeight(getApp().getConf().getConfs("web/mount"));
        for (Conf x : z) {
            Mount mp = getApp().create(x, MountVfs.class);
            addMount(mp);
        }

    }

    protected void addMount(Mount mp) throws Exception {
        if (mp instanceof IMountProvider) {
            List<Mount> z = ((IMountProvider) mp).loadMounts();
            if (z != null) {
                for (Mount m : z) {
                    addMount(m);
                }
            }
        } else if (mp instanceof VirtFSMount) {
            virtFS.addMount((VirtFSMount) mp);
        } else {
            throw new XError("mount объект должен быть или IMountProvider или VirtFSMount");
        }
    }

    public VirtFile findFile(String path) {
        return virtFS.findFile(path);
    }

    public List<VirtFile> findFiles(String path) {
        return virtFS.findFiles(path);
    }

    public VirtFile getFile(String path) {
        VirtFile f = findFile(path);
        if (f == null || f.isFolder()) {
            throw new XError("Файл не найден: {0}", path);
        }
        return f;
    }

    public List<Mount> getMounts() {
        List<Mount> res = new ArrayList<>();
        res.addAll(virtFS.getMounts());
        return res;
    }

    public VirtFile wrapFile(FileObject realFile) {
        return VirtFileVFS.create(realFile, "", fileTypeHolder, false);
    }

    public VirtFile wrapFile(String realFileName) {
        return wrapFile(UtFile.getFileObject(realFileName));
    }

    //////

    public FileType findFileType(String name) {
        return fileTypeHolder.find(name);
    }

    public List<FileType> getFileTypes() {
        return fileTypeHolder.getItems();
    }

    ////// ICheckChangedResource

    public void checkChangedResource(CheckChangedResourceInfo info) throws Exception {
        // простая реализация: считаем что в production состав файлов не меняется вообще
        // в debug просто ощищаем кеш
        if (getApp().isDebug() && !getApp().isTest()) {
            virtFS.clearCache();
        }
    }

    //////

    public DirScanner<VirtFile> createDirScanner(String dir) {
        return new DirScannerVirtFS(virtFS, dir);
    }

}
