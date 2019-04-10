package jandcode.mdoc.groovy;

import jandcode.commons.*;
import jandcode.commons.groovy.*;
import jandcode.core.*;
import jandcode.core.groovy.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.gsp.*;
import jandcode.mdoc.source.*;

import java.util.*;

/**
 * Фабрика классо по исходникам groovy/gsp
 */
public class GroovyFactory extends BaseOutBuilderMember implements IBeanConfigure {

    private Map<String, GroovyClazz> cacheCls = new HashMap<>();
    private GroovyCompiler compiler;

    public void beanConfigure(BeanConfig cfg) throws Exception {
        this.compiler = getOutBuilder().getApp().bean(GroovyService.class).getGroovyCompiler("mdoc");
    }

    /**
     * Создает template по файлу gsp из документа
     */
    public BaseGspTemplate createTemplate(String path) throws Exception {
        path = UtVDir.normalize(path);
        GroovyClazz clazz = cacheCls.get(path);
        if (clazz == null) {
            SourceFile sf = getOutBuilder().getDoc().getSourceFiles().get(path);
            if (!UtString.empty(sf.getRealPath())) {
                clazz = compiler.getClazz(BaseGspTemplate.class, "void onGenerate()", UtFile.getFileObject(sf.getRealPath()), true);
            } else {
                clazz = compiler.getClazz(BaseGspTemplate.class, "void onGenerate()", sf.getText(), true);
            }
            cacheCls.put(path, clazz);
        }
        return (BaseGspTemplate) getOutBuilder().create(clazz.getClazz());
    }

    /**
     * Создает объект по файлу groovy.
     * Текст файла groovy должен быть полным классом, т.е. содержать объявление класса,
     * экземпляр которого и вернется.
     */
    public Object createObject(String path) throws Exception {
        path = UtVDir.normalize(path);
        GroovyClazz clazz = cacheCls.get(path);
        if (clazz == null) {
            SourceFile sf = getOutBuilder().getDoc().getSourceFiles().get(path);
            if (!UtString.empty(sf.getRealPath())) {
                clazz = compiler.getClazz(Object.class, GroovyCompiler.SIGN_CLASS, UtFile.getFileObject(sf.getRealPath()), false);
            } else {
                clazz = compiler.getClazz(Object.class, GroovyCompiler.SIGN_CLASS, sf.getText(), false);
            }
            cacheCls.put(path, clazz);
        }
        return getOutBuilder().create(clazz.getClazz());
    }

}
