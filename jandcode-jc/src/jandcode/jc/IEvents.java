package jandcode.jc;

import groovy.lang.*;
import jandcode.commons.event.*;

/**
 * События проекта и контекста.
 */
public interface IEvents extends EventBus {

    /**
     * Подписаться на событие
     *
     * @param eventClass класс события
     * @param handler    обработчик события
     */
    void onEvent(Class eventClass, Closure handler);

}
