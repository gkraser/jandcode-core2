package jandcode.core.jsa.jc;

import groovy.json.*;

/**
 * UtJson с реализаций на groovy.
 * Порядок свойств сохраняется.
 */
public class JsaUtJson {

    public static String toJson(Object value) {
        return JsonOutput.toJson(value);
    }

    public static Object fromJson(String s) {
        JsonSlurper jsonSlurper = new JsonSlurper();
        return jsonSlurper.parseText(s);
    }

}
