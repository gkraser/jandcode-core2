package jandcode.commons.error;

import java.util.*;

/**
 * Конвертор Exception.
 */
public interface ErrorConvertor {

    /**
     * Получить текст для Throwable
     *
     * @param e
     * @return текст или null, если неподдерживаемый тип ошибки
     */
    String getText(Throwable e);

    /**
     * Возвращает реальный Throwable, пропуская всякие ненужные обертки
     *
     * @param e предпологаемая ошибка
     * @return реальная ошибка или null, если неподдерживаемый тип ошибки
     */
    Throwable getReal(Throwable e);

    /**
     * Возвращает следующий Throwable в стеке после указанного
     *
     * @param e
     * @return следующая ошибка или null, если неподдерживаемый тип ошибки
     */
    Throwable getNext(Throwable e);

    /**
     * Возвращает true, если ошибка является маркером, а не реальной ошибкой
     *
     * @param e
     * @return true, если маркер. false - если не является маркером с точки зрения этого конвертора
     */
    Boolean isMark(Throwable e);

    /**
     * Возвращает описания строк исходников, которые связана с ошибкой
     *
     * @param e ошибка
     * @return null, если с ошибкой не связаны исходники либо их нельзя определеить
     */
    List<ErrorSource> getErrorSource(Throwable e);

    /**
     * Возвращает true, если класс нужно исключить из отфильтрованного стека.
     *
     * @param className полное имя проверяемого класса
     */
    boolean isFiltered(String className);

}
