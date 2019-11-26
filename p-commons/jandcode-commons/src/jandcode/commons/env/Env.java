package jandcode.commons.env;

import jandcode.commons.variant.*;

/**
 * Среда исполнения (prod/dev)
 */
public interface Env {

    /**
     * Среда разработки.
     * Проект запускается в режиме отладки.
     */
    boolean isDev();

    /**
     * Среда в исходниках.
     * Проект запускается из исходников.
     */
    boolean isSource();

    /**
     * Среда тестов.
     * Означает, что сейчас запускаются тесты.
     */
    boolean isTest();

    /**
     * Все доступные свойства среды.
     * Сюда включены все System.properties, System.getenv() и определенные средой значения.
     * Приоритет (первый побеждает): System.properties -> System.getenv() -> определенные средой.
     */
    IVariantMap getProperties();

}
