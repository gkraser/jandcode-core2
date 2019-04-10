package jandcode.commons.reflect.impl.convs;


import jandcode.commons.*;
import jandcode.commons.reflect.*;

public class PropConvertorBoolean implements ReflectPropConvertor {

    public Object fromString(String s) {
        return UtCnv.toBoolean(s);
    }

}
