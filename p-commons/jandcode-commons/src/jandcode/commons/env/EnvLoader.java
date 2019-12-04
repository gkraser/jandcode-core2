package jandcode.commons.env;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.variant.*;
import org.slf4j.*;

import java.util.*;

public class EnvLoader {

    protected static Logger log = LoggerFactory.getLogger(EnvLoader.class);

    public Env load(String filename, boolean test) {
        VariantMap props = new VariantMap();
        //

        // .env files
        String path = UtFile.path(filename);
        String name = UtFile.filename(filename);

        appendFile(props, filename);
        appendFile(props, UtFile.join(path, "_" + name));

        // enviroment vars
        Map<String, String> osenv = System.getenv();
        for (String key : osenv.keySet()) {
            String keyUp = key.toUpperCase();
            props.put(keyUp, osenv.get(key));
        }

        // System.properties
        Enumeration<?> pn = System.getProperties().propertyNames();
        while (pn.hasMoreElements()) {
            String key = UtString.toString(pn.nextElement());
            props.put(key, System.getProperty(key));
        }

        boolean dev = props.getBoolean(UtilsConsts.PROP_ENV_DEV, false);
        boolean source = props.getBoolean(UtilsConsts.PROP_ENV_SOURCE, false);

        return new DefaultEnv(dev, source, test, props);
    }

    private static class PropertiesWrap extends Properties {
        private Map<String, String> map = new LinkedHashMap<>();

        public Map<String, String> getMap() {
            return map;
        }

        public synchronized Object put(Object key, Object value) {
            map.put(UtString.toString(key), UtString.toString(value));
            return super.put(key, value);
        }
    }

    protected void appendFile(VariantMap props, String filename) {
        if (UtFile.exists(filename)) {
            PropertiesWrap pp = new PropertiesWrap();
            try {
                new PropertiesLoader(pp).load().fromFile(filename);
            } catch (Exception e) {
                log.error("loadEnv", e);
            }
            props.putAll(pp.getMap());
        }
    }

}
