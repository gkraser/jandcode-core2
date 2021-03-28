package jandcode.commons.io;

import java.io.*;
import java.util.*;

/**
 * Загрузка Properties из различных источников. Использование:
 * <pre>
 * Properties p = new Properties()
 * new PropertiesLoader(p).load().fromFile("filename.properties")
 * </pre>
 */
public class PropertiesLoader implements ILoader {

    private Properties _properties;

    public PropertiesLoader(Properties properties) {
        _properties = properties;
    }

    public void loadFrom(Reader reader) throws Exception {
        _properties.load(reader);
    }
}
