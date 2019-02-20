package jandcode.commons.json.impl.jsonsimple.ext;

import java.util.*;

/**
 * Менеджер дополнительных конверторов.
 * Введен фактически из-за Date. Можно регистрировать собственные конверторы,
 * но вряд ли это нужно.
 */
public class JSONValueConvertorManager {

    private static JSONValueConvertorManager inst = new JSONValueConvertorManager();
    private List<JSONValueConvertor> convertors = new ArrayList<>();

    static {
        getInst().registerConvertor(new JSONValueConvertor_Date());
    }

    public static JSONValueConvertorManager getInst() {
        return inst;
    }

    public void registerConvertor(JSONValueConvertor cnv) {
        convertors.add(0, cnv);
    }

    public String toJsonString(Object v) {
        for (JSONValueConvertor c : convertors) {
            String res = c.toJsonString(v);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

}
