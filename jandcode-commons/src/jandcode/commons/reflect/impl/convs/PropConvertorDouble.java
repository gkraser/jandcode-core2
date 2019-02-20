package jandcode.commons.reflect.impl.convs;


import jandcode.commons.*;
import jandcode.commons.reflect.*;

public class PropConvertorDouble implements ReflectPropConvertor {

    public Object fromString(String s) {
        return UtCnv.toDouble(s);
    }

}
