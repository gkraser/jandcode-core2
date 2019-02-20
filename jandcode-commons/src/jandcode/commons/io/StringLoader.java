package jandcode.commons.io;

import java.io.*;

/**
 * Loader для загрузки строки из разных источников
 */
public class StringLoader implements ILoader {

    private static int BUFSIZE = 8192;
    private StringBuilder sb = new StringBuilder();

    /**
     * Загруженная строка
     */
    public String getResult() {
        return sb.toString();
    }

    public LoadFrom load() {
        return new LoadFrom(this);
    }

    public void loadFrom(Reader reader) throws Exception {
        Reader rdr = reader;
        char[] b = new char[BUFSIZE];
        int n;
        while ((n = rdr.read(b)) > 0) {
            sb.append(b, 0, n);
        }
    }
}
