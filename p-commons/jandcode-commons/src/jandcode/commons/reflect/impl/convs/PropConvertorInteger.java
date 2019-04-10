package jandcode.commons.reflect.impl.convs;


import jandcode.commons.*;
import jandcode.commons.reflect.*;


public class PropConvertorInteger implements ReflectPropConvertor {

    public Object fromString(String s) {
        return UtCnv.toInt(s);
    }

}
