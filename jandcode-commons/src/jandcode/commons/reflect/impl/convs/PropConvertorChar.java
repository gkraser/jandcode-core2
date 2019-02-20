package jandcode.commons.reflect.impl.convs;

import jandcode.commons.reflect.*;

public class PropConvertorChar implements ReflectPropConvertor {

    public Object fromString(String s) {
        if (s.length() == 0) {
            return (char) 0;
        }
        return s.charAt(0);
    }

}
