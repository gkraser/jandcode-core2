package jandcode.commons.rnd;

import java.util.*;

/**
 * Набор утилит для генерации случайных данных.
 */
public interface IRnd {

    /**
     * Установить начальное значение генератора Random для получения
     * предсказуемых последовательностей.
     */
    void setSeed(long seed);

    /**
     * Получить расширение RndExt по классу
     *
     * @param cls какое расширение требуется
     * @return кешированный экземпляр расширения
     */
    <A extends RndExt> A getExt(Class<A> cls);

    /**
     * Создать новый экземпляр расширения RndExt по классу
     *
     * @param cls какое расширение требуется
     * @return новый экземпляр расширения
     */
    <A extends RndExt> A createExt(Class<A> cls);

    /**
     * true/false с вероятностью 50%
     */
    boolean bool();

    /**
     * true с вероятностью t/f
     */
    boolean bool(int t, int f);

    /**
     * true с вероятностью t/1
     */
    boolean bool(int t);

    /**
     * int в указанном диапазоне
     */
    int num(int min, int max);

    /**
     * double в указанном диапазоне с указанным числом знаков после запятой (scale).
     */
    double doub(double min, double max, int scale);

    /**
     * Один из символов из chars
     */
    char choice(CharSequence chars);

    /**
     * Один из объектов из items
     */
    Object choice(List items);

    /**
     * Одна из строк из items
     */
    String choice(String[] items);

    /**
     * Текст
     *
     * @param chars    набор символов
     * @param min      минимальная длина
     * @param max      максимальная длина
     * @param wordSize примерный размер слова
     * @return текст
     */
    String text(String chars, int min, int max, int wordSize);
}
