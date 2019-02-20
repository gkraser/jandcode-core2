package jandcode.jc;

/**
 * Версия.
 * Метод toString() должен возвращать getText().
 */
public interface IVersion {

    /**
     * Возвращает версию в виде текста
     */
    String getText();

    /**
     * true - это версия заглушка.
     */
    boolean isDummy();

}
