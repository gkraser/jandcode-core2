package jandcode.jc.std;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;
import org.apache.commons.vfs2.*;

import java.io.*;
import java.util.*;
import java.util.jar.*;

/**
 * Кешировщик для jar.
 */
public class JarCacheService extends CtxService {

    private Map<String, JarEntry> cache = new HashMap<>();

    class JarEntry {
        private String key;
        private String jarFile;
        private String cacheDir;
        private String contentDir;
        private Manifest manifest;

        public JarEntry(String jarFile) {
            this.jarFile = jarFile;
        }

        private void init() throws Exception {
            File f = new File(jarFile);
            key = f.getName() + "--" + UtString.md5Str(f.getName() + "|" + f.length() + "|" + f.lastModified());
            String bd = getCtx().getTempdirCommon("jars" + "/" + key.substring(0, 2));
            cacheDir = UtFile.join(bd, key);
            //
            File okFile = new File(UtFile.join(cacheDir, ".ok-" + key));
            if (!okFile.exists()) {
                buildCache();
                UtFile.saveString(f.getAbsolutePath(), okFile);
            }
            okFile = new File(UtFile.join(cacheDir, ".ok-content-" + key));
            if (okFile.exists()) {
                contentDir = UtFile.join(cacheDir, "content");
            }
        }

        private void buildCache() throws Exception {
            getCtx().getLog().debug("build cache for: " + jarFile);
            UtFile.cleanDir(cacheDir);
            String mf = getMetaInfDir();
            UtFile.mkdirs(mf);
            FileObject destMF = UtFile.getFileObject(mf);
            //
            FileObject srcMF = UtFile.getFileObject("jar:file:/" + jarFile.replace("\\", "/") + "!/META-INF");
            destMF.copyFrom(srcMF, new AllFileSelector());
        }

        private void buildContentDir() throws Exception {
            getCtx().getLog().debug("build content cache for: " + jarFile);
            String contd = UtFile.join(cacheDir, "content");
            UtFile.cleanDir(contd);
            FileObject destMF = UtFile.getFileObject(contd);
            //
            FileObject srcMF = UtFile.getFileObject("jar:file:/" + jarFile.replace("\\", "/") + "!/");
            destMF.copyFrom(srcMF, new AllFileSelector());
            //
            File okFile = new File(UtFile.join(cacheDir, ".ok-content-" + key));
            UtFile.saveString("ok", okFile);
            contentDir = contd;
        }

        public String getMetaInfDir() {
            return UtFile.join(cacheDir, "META-INF");
        }

        public String getJcDataDir() {
            return UtFile.join(getMetaInfDir(), JcConsts.JC_DATA_DIR);
        }

        public String getContentDir() {
            if (contentDir == null) {
                try {
                    buildContentDir();
                } catch (Exception e) {
                    throw new XErrorWrap(e);
                }
            }
            return contentDir;
        }

        public boolean hasJcData() {
            return UtFile.exists(getJcDataDir());
        }

        public Manifest getManifest() {
            if (manifest == null) {
                String mdir = getMetaInfDir();
                String mfile = UtFile.join(mdir, "MANIFEST.MF");
                if (UtFile.exists(mfile)) {
                    try {
                        InputStream inps = new FileInputStream(mfile);
                        try {
                            manifest = new Manifest(inps);
                        } finally {
                            inps.close();
                        }
                    } catch (IOException e) {
                        getCtx().getLog().warn("MANIFEST read error: " + mfile);
                        manifest = new Manifest();
                    }
                } else {
                    manifest = new Manifest();
                }
            }
            return manifest;
        }
    }

    private JarEntry getJarEntry(String jarFile) {
        if (getCtx().getConfig().isRunAsProduct()) {
            getCtx().getLog().warn("jar кешируется в product-mode: " + jarFile);
        }
        JarEntry e = cache.get(jarFile);
        if (e == null) {
            e = new JarEntry(jarFile);
            try {
                e.init();
            } catch (Exception e1) {
                throw new XErrorWrap(e1);
            }
            cache.put(jarFile, e);
        }
        return e;
    }

    //////

    /**
     * Возвращает каталог с кешированным содержимым каталога META-INF из jar
     */
    public String getMetaInfDir(String jarFile) {
        JarEntry en = getJarEntry(jarFile);
        return en.getMetaInfDir();
    }

    /**
     * Имеется ли в META-INF jar файл или каталог jc-данных
     */
    public boolean hasJcData(String jarFile) {
        JarEntry en = getJarEntry(jarFile);
        return en.hasJcData();
    }

    /**
     * Возвращает каталог с кешированным содержимым каталога jc-данных из jar
     */
    public String getJcDataDir(String jarFile) {
        JarEntry en = getJarEntry(jarFile);
        return en.getJcDataDir();
    }

    /**
     * Возвращает каталог с кешированным содержимым jar
     */
    public String getContentDir(String jarFile) {
        JarEntry en = getJarEntry(jarFile);
        return en.getContentDir();
    }

    /**
     * Возвращает манифест из кешированного содержимымого jar
     */
    public Manifest getManifest(String jarFile) {
        JarEntry en = getJarEntry(jarFile);
        return en.getManifest();
    }

}
