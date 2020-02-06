package jandcode.commons.launcher;

import java.lang.reflect.*;
import java.util.*;

/**
 * Запускалка с переопределением classloader.
 */
public class Launcher {

    public static final String P_CLASSPATH = "jandcode.launcher.classpath";
    public static final String P_MAIN = "jandcode.launcher.main";
    public static final String P_DEBUG = "jandcode.launcher.debug";

    public static void main(String[] args) throws Exception {
        Launcher z = new Launcher();
        z.run(args);
    }

    //////

    private JcURLClassLoader curClassLoader;
    private boolean debug;

    public void run(String[] args) throws Exception {

        // создаем новый class loader
        curClassLoader = new JcURLClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(curClassLoader);

        this.debug = getDebug();
        String mainClass = getMainClass();
        List<String> classpath = getClasspath();

        curClassLoader.setDebug(this.debug);

        for (String p : classpath) {
            curClassLoader.addPath(p);
        }

        runMain(mainClass, args);
    }

    //////

    public String getMainClass() {
        String v = System.getProperty(P_MAIN);
        if (this.debug) {
            System.out.println(getClass().getSimpleName() + ":main: " + v);
        }
        if (v == null || v.length() == 0) {
            throw new RuntimeException("Not defined main class in property: " + P_MAIN);
        }
        return v;
    }

    public List<String> getClasspath() {
        String v = System.getProperty(P_CLASSPATH);
        if (this.debug) {
            System.out.println(getClass().getSimpleName() + ":classpath: " + v);
        }
        if (v == null) {
            throw new RuntimeException("Not defined classpath in property: " + P_CLASSPATH);
        }
        List<String> res = new ArrayList<>();
        String pathSep = System.getProperty("path.separator");

        StringTokenizer tk = new StringTokenizer(v, pathSep);
        while (tk.hasMoreTokens()) {
            String s1 = tk.nextToken().trim();
            if (s1.length() > 0) {
                res.add(s1);
            }
        }

        return res;
    }

    public boolean getDebug() {
        String v = System.getProperty(P_DEBUG);
        return "true".equals(v);
    }

    public void runMain(String mainClass, String[] args) throws Exception {
        Class<?> cls = curClassLoader.loadClass(mainClass);
        Method m = cls.getMethod("main", String[].class);
        m.invoke(null, new Object[]{args});
    }

}
