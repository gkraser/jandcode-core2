package jandcode.commons.launcher;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 */
public class JcURLClassLoader extends URLClassLoader {

    private Set<URL> used = new LinkedHashSet<>();

    public JcURLClassLoader(ClassLoader parent) {
        super(new URL[]{}, parent);
    }

    /**
     * Метод заменяет текущий classloader на JcURLClassLoader.
     */
    public static JcURLClassLoader fixThisCurrent() {
        ClassLoader curLdr = Thread.currentThread().getContextClassLoader();
        if (!(curLdr instanceof JcURLClassLoader)) {
            JcURLClassLoader newLdr = new JcURLClassLoader(curLdr);
            Thread.currentThread().setContextClassLoader(newLdr);
            return newLdr;
        } else {
            return (JcURLClassLoader) curLdr;
        }
    }

    /**
     * Добавить путь к текущему classpath
     *
     * @param path какой путь
     * @return true, если путь был добавлен. false - если уже был добален ранее
     */
    public static boolean addClasspath(String path) {
        if (path == null || path.length() == 0) {
            return false;
        }
        //
        JcURLClassLoader ldr = fixThisCurrent();
        try {
            return ldr.addPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает список из всех classpath на момент вызова
     */
    public static List<String> getClasspath() {
        List<String> res = new ArrayList<>();

        JcURLClassLoader ldr = fixThisCurrent();
        for (URL u : ldr.getURLs()) {
            String p = u.getPath();
            if (p == null || p.length() == 0) {
                continue;
            }
            String fn = new File(p).getAbsolutePath();
            res.add(fn);
        }
        return res;
    }

    public void addURL(URL url) {
        if (used.contains(url)) {
            return;
        }
        used.add(url);
        super.addURL(url);
    }

    public boolean addPath(String path) throws Exception {
        File f = new File(path);
        URL u = f.toURI().toURL();
        if (used.contains(u)) {
            return false;
        }
        addURL(u);
        return true;
    }

}
