package jandcode.jc.launcher;

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
