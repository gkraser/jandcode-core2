package jandcode.commons.json.impl.jsonsimple.ext;


import jandcode.commons.*;
import jandcode.commons.datetime.*;

import java.util.*;

public class JSONValueConvertor_Date implements JSONValueConvertor {

    public String toJsonString(Object value) {
        if (value instanceof Date) {
            value = UtCnv.toDateTime(value);
        }
        if (value instanceof XDateTime) {
            return value.toString();
        }
        return null;

    }
}
