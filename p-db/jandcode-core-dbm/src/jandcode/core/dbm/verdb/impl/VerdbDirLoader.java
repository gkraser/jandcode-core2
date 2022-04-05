package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.core.dbm.verdb.*;
import org.apache.commons.vfs2.*;

import java.util.*;

/**
 * Загрузчик данных из каталога
 */
public class VerdbDirLoader {

    /**
     * Загрузить все из указанного каталога
     *
     * @param path откуда грузим
     * @return набор правильно отсортированных каталогов вместе со всем содержимым
     */
    public List<VerdbDir> loadDir(String path) {
        Map<String, VerdbDirImpl> tmp = new HashMap<>();

        DirScanner<FileObject> sc = UtFile.createDirScannerVfs(path + "/*");
        sc.setNeedDirs(true);
        sc.setNeedFiles(false);

        List<FileObject> dirs = sc.load();

        for (FileObject dir : dirs) {
            String dirS = dir.toString();
            String nm = UtFile.filename(dirS);

            long version1 = VerdbUtils.extractVersionFromFilename(nm);
            if (version1 < 0) {
                continue; // не каталог с версией
            }
            if (version1 == 0) {
                throw new XError("Версия каталога не должна быть '0': [{0}]", dirS);
            }

            VerdbDirImpl d = new VerdbDirImpl(dirS, version1);

            String key = d.getVersion().getText();
            VerdbDirImpl dExist = tmp.get(key);
            if (dExist != null) {
                throw new XError("Дублирование версии каталога {0}: [{1}], [{2}]",
                        version1, dirS, dExist.getPath());
            }

            tmp.put(d.getVersion().getText(), d);

            // теперь грузим файлы в каталоге
            List<VerdbFile> files = loadFiles(d, version1);
            d.setFiles(files);
        }

        List<VerdbDir> res = new ArrayList<>(tmp.values());
        res.sort(null);

        return res;
    }

    protected List<VerdbFile> loadFiles(VerdbDir dir, long version1) {
        Map<String, VerdbFileImpl> tmp = new HashMap<>();

        DirScanner<FileObject> sc = UtFile.createDirScannerVfs(dir.getPath() + "/*");
        sc.setNeedDirs(false);
        sc.setNeedFiles(true);

        List<FileObject> files = sc.load();

        for (FileObject file : files) {
            String fileS = file.toString();
            String nm = UtFile.filename(fileS);

            long version2 = VerdbUtils.extractVersionFromFilename(nm);
            if (version2 < 0) {
                continue; // не файл с версией
            }
            if (version2 == 0) {
                throw new XError("Версия файла не должна быть '0': [{0}]", fileS);
            }

            VerdbFileImpl f = new VerdbFileImpl(dir, fileS, version1, version2);

            String key = f.getVersion().getText();
            VerdbFileImpl fExist = tmp.get(key);
            if (fExist != null) {
                throw new XError("Дублирование версии файла {0}: [{1}], [{2}]",
                        version2, fileS, fExist.getPath());
            }

            tmp.put(f.getVersion().getText(), f);
        }

        List<VerdbFile> res = new ArrayList<>(tmp.values());
        res.sort(null);

        return res;
    }

}
