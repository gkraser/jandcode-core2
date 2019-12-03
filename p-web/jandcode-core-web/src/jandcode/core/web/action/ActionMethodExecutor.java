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
     * @param inst   экземпляр action (обычно {@link BaseAction})
     * @param method метод, который нужно выполнить в inst
     */
    void execActionMethod(Object inst, Method method) throws Exception;

}
