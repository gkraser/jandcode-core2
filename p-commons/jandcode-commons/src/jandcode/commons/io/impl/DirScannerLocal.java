package jandcode.commons.io.impl;

import jandcode.commons.*;

import java.io.*;
import java.util.*;

public class DirScannerLocal extends BaseDirScanner<File> {

    protected File createItem(String dir) {
        return new File(dir);
    }

    protected boolean isFile(File f) {
        return f.isFile();
    }

    protected List<File> listDir(File f) {
        File[] ar = f.listFiles();
        if (ar == null || ar.length == 0) {
            // тут ничего нет
            return null;
        }
        List<File> lst = Arrays.asList(ar);
        lst.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return lst;
    }

    protected String getName(File f) {
        return f.getName();
    }

    protected boolean isCaseSensitive(File f) {
        return !UtFile.isWindows();
    }

}
