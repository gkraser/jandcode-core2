package jandcode.jc.impl.lib;

import jandcode.jc.*;
import jandcode.jc.std.*;

/**
 * Провайдер для JavaProject
 */
public class LibProviderJavaProject extends LibProviderCustom {

    private JavaProject javaProject;

    public LibProviderJavaProject(JavaProject javaProject) {
        this.javaProject = javaProject;
        this.path = javaProject.getProject().getWd().getPath();
    }

    protected void doFillLibs(ListLib libs) {
        libs.add(new LibJavaProject(javaProject));
    }

}
