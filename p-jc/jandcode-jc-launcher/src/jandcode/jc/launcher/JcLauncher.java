package jandcode.jc.launcher;

import java.lang.reflect.*;
import java.util.*;

/**
 * Запускалка jc.
 */
public class JcLauncher {

    public static final String P_CLASSPATH = "jandcode.jc.classpath";
    public static final String P_MAIN = "jandcode.jc.main";

    public static void main(String[] args) throws Exception {
        JcLauncher z = new JcLauncher();
        z.run(args);
    }

    //////

    private JcURLClassLoader curClassLoader;

    public void run(String[] args) throws Exception {

        // создаем новый class loader
        curClassLoader = new JcURLClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(curClassLoader);

        String mainClass = getMainClass();
        List<String> classpath = getClasspath();

        for (String p : classpath) {
            curClassLoader.addPath(p);
        }

        runMain(mainClass, args);
    }

    //////

    public String getMainClass() {
        String v = System.getProperty(P_MAIN);
        if (v == null || v.length() == 0) {
            throw new RuntimeException("Not defined main class in property: " + P_MAIN);
        }
        return v;
    }

    public List<String> getClasspath() {
        String v = System.getProperty(P_CLASSPATH);
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

    public void runMain(String mainClass, String[] args) throws Exception {
        Class<?> cls = curClassLoader.loadClass(mainClass);
        Method m = cls.getMethod("main", String[].class);
        m.invoke(null, new Object[]{args});
    }

}
