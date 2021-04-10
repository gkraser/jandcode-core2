package jandcode.jc.nodejs;

import groovy.json.*;

/**
 * UtJson с реализаций на groovy.
 * Порядок свойств сохраняется.
 */
public class NodeJsUtJson {

    public static String toJson(Object value) {
        return JsonOutput.toJson(value);
    }

    public static String toJsonPretty(Object value) {
        return JsonOutput.prettyPrint(toJson(value));
    }

    public static Object fromJson(String s) {
        JsonSlurper jsonSlurper = new JsonSlurper();
        return jsonSlurper.parseText(s);
    }

}
