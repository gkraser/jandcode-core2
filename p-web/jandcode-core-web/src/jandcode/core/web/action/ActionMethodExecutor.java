package jandcode.core.web.action;

import java.lang.reflect.*;

/**
 * Если action имеет один параметр с наследником этого типа,
 * то создается этот экземпляр и ему делегируется выполнения метода action.
 */
public interface ActionMethodExecutor {

    /**
     * Выполнить метод action
     *
     * @param action экземпляр action
     * @param method метод, который нужно выполнить в inst
     */
    void execActionMethod(BaseAction action, Method method) throws Exception;

}
