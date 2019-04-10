package jandcode.commons.reflect.impl.convs;

import jandcode.commons.*;
import jandcode.commons.reflect.*;

public class PropConvertorClass implements ReflectPropConvertor {

    public Object fromString(String s) {
        return UtClass.getClass(s);
    }

}