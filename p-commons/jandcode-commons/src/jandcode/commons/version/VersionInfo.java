package jandcode.commons.version;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.commons.io.*;
import org.apache.commons.vfs2.*;

import java.util.*;

/**
 * Информация о версии приложения. Версия загружается для package. Подразумевается,
 * что в нем есть файл version.properties со строкой version
 */
public class VersionInfo {

    /**
     * Имя файла с информацией о версии
     */
    public static final String VERSION_FILE = "version.properties"; //NON-NLS

    /**
     * Версия по умолчанию, возвращается, когна неудается определить версию
     */
    public static final String DEFAULT_VERSION = "SNAPSHOT"; //NON-NLS


    private String version;
    private XDateTime buildDate;

    /**
     * Инициализирует объекта версией указанного пакета
     */
    public VersionInfo(String aPackage) {
        init(aPackage);
    }

    /**
     * Версия
     */
    public String getVersion() {
        return version == null ? DEFAULT_VERSION : version; //NON-NLS
    }

    /**
     * Дата сборки. Определяется по времени создания version.properties
     */
    public XDateTime getBuildDate() {
        return buildDate;
    }

    //////

    /**
     * Инициализация для указанно пакета
     */
    private void init(String pak) {
        Properties prop = loadVersionProperties(pak);
        if (prop != null) {
            this.version = prop.getProperty("version");
            if (UtString.empty(this.version)) {
                this.version = null;
            } else {
                this.version = this.version.trim();
            }
            this.buildDate = XDateTime.create(prop.getProperty("buildDate"));
        }
    }

    /**
     * Загружает version.properties начиная с пакета fromPak и вверх по иерархии
     * пакетов. Первый найденный загружается.
     *
     * @return null, если не найден
     */
    private Properties loadVersionProperties(String fromPak) {
        List<String> lst = new ArrayList<>(Arrays.asList(fromPak.split("\\.")));
        while (lst.size() > 0) {
            String pak = UtString.join(lst, ".");
            Properties prop = loadVersionPropertiesFile(pak);
            if (prop != null) {
                return prop;
            }
            lst.remove(lst.size() - 1);
        }
        return null;
    }

    /**
     * Загружает version.properties из указанного пакета.
     *
     * @return null, если не найден файл
     */
    private Properties loadVersionPropertiesFile(String pak) {
        try {
            String res = "res:" + pak.replace('.', '/') + "/" + VERSION_FILE; //NON-NLS
            FileObject f = UtFile.getFileObject(res);
            if (f.exists()) {
                Properties prop = new Properties();
                PropertiesLoader ldr = new PropertiesLoader(prop);
                UtLoad.fromFileObject(ldr, f);
                XDateTime buildDate = XDateTime.create(f.getContent().getLastModifiedTime());
                buildDate = buildDate.clearMSec();
                prop.setProperty("buildDate", buildDate.toString());
                return prop;
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

}
