package jandcode.commons.reflect.impl.convs;

import jandcode.commons.reflect.*;

import java.io.*;


public class PropConvertorFile implements ReflectPropConvertor {

    public Object fromString(String s) {
        return new File(s);
    }

}