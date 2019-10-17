package jandcode.core.web.filter;

import jandcode.core.web.*;

/**
 * Типы фильтров, которые вызываются в процессе обработки запроса.
 */
public enum FilterType {

    /**
     * Вызывается в начале обработки запроса. request создан, action еще не определена.
     */
    startRequest,

    /**
     * Вызывается в конце обработки запроса. Все обработано, возможно были ошибки.
     * Если была ошибка, то она в {@link Request#getException()}.
     */
    stopRequest,

    /**
     * Вызывается перед выполнением action, которая уже известна и находится
     * в {@link Request#getAction()}
     */
    beforeAction,

    /**
     * Вызывается после выполнения action {@link Request#getAction()}
     */
    afterAction,

    /**
     * Вызывается перед выполнением render, который уже известен и находится
     * в {@link Request#getRender()}
     */
    beforeRender,

    /**
     * Вызывается после выполнения render {@link Request#getRender()}
     */
    afterRender


}
