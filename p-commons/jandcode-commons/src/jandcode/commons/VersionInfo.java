package jandcode.commons;

import jandcode.commons.io.*;
import org.apache.commons.vfs2.*;

import java.util.*;

/**
 * Информация о версии приложения. Версия загружается для package. Подразумевается,
 * что в нем есть файл version.properties со строкой version
 */
public class VersionInfo {

    private static final String VERSION_FILE = "version.properties"; //NON-NLS

    private String _version;

    /**
     * Инициализирует объект версией указанного пакета
     */
    public VersionInfo(String aPackage) {
        loadVersion(aPackage);
    }

    /**
     * Версия
     */
    public String getVersion() {
        return _version == null ? "SNAPSHOT" : _version; //NON-NLS
    }

    private void loadVersion(String aPackage) {
        _version = null;
        try {
            String res = "res:" + aPackage.replace('.', '/') + "/" + VERSION_FILE; //NON-NLS
            FileObject f = UtFile.getFileObject(res);
            if (f.exists()) {
                Properties prop = new Properties();
                PropertiesLoader ldr = new PropertiesLoader(prop);
                UtLoad.fromFileObject(ldr, f);
                _version = prop.getProperty("version");
                if (_version != null && _version.length() == 0) {
                    _version = null;
                }
            }
        } catch (Exception e) {
        }
    }

}
