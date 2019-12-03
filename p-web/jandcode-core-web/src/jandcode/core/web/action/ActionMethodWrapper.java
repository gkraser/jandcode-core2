package jandcode.core.web.action;

import java.lang.reflect.*;

/**
 *
 */
public interface ActionMethodWrapper {

    void execActionMethod(Object inst, Method method) throws Exception;

}
