package jandcode.commons.ansifer.impl;

import jandcode.commons.*;
import org.apache.commons.vfs2.*;

import java.io.*;
import java.util.*;

/**
 * Извлечение нативных библиотек jansi
 */
public class JansiExtractor {

    private String getJansiPomPath() {
        String path = "org.fusesource.jansi/jansi";
        return "/META-INF/maven/" + path + "/pom.properties";
    }

    /**
     * Загрузить версию jansi
     */
    public String loadJansiVersion() throws Exception {
        InputStream in = JansiExtractor.class.getResourceAsStream(getJansiPomPath());
        String defaultVersion = "0.0";
        if (in == null) {
            return defaultVersion;
        }
        try {
            Properties p = new Properties();
            p.load(in);
            return p.getProperty("version");
        } catch (Exception e) {
            return defaultVersion;
        } finally {
            in.close();
        }
    }

    /**
     * Извлечь нативные библиотеки в указанный каталог.
     * Без всяких проверок, только процесс.
     */
    public void extractNativeLibs(String todir) throws FileSystemException {
        String s = JansiExtractor.class.getResource(getJansiPomPath()).toString();
        int b = s.indexOf('!');
        s = s.substring(0, b) + "!/META-INF/native";

        FileObject fdest = UtFile.getFileObject(todir);
        FileObject fsrc = UtFile.getFileObject(s);
        fdest.copyFrom(fsrc, new AllFileSelector());
    }


    /**
     * Извлечь нативные библиотеки jansi с кешированием.
     *
     * @return путь, куда излекли
     */
    public String extract() throws Exception {
        String version = loadJansiVersion();
        String destPath = UtFile.join(UtFile.getTempdir(), ".jandcode-cache/jansi-native", version);
        String okFile = UtFile.join(destPath, ".ok");
        if (!UtFile.exists(okFile)) {
            extractNativeLibs(destPath);
            UtFile.saveString("ok", new File(okFile));
        }
        return destPath;
    }

}
