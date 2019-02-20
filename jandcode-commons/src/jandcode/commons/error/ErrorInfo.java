package jandcode.commons.error;

import java.util.*;

/**
 * Информация об ошибке, преобразованная к удобному виду
 */
public interface ErrorInfo {

    /**
     * Оригинальная ошибка
     */
    Throwable getException();

    /**
     * Тект ошибки в виде строки
     */
    String getText();

    /**
     * Получить текст стека
     *
     * @param filtered true - отфильтрованный
     */
    String getTextStack(boolean filtered);

    /**
     * Получить текст для идентификации исходников, где возникла ошибка.
     * Обычно используется для скриптов.
     */
    String getTextErrorSource();

    /**
     * Линейный список exception, сформированный по корневому и включающий в себя
     * все нужные в стеке exception. В этом списке уже отфильтрованы все известные
     * обертки и вспомогательные exception.
     */
    List<Throwable> getExceptions();

}
