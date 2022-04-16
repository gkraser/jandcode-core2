package jandcode.commons.rnd;

import jandcode.commons.datetime.*;
import jandcode.commons.rnd.impl.*;

import java.util.*;

/**
 * Набор утилит для генерации случайных данных.
 */
public interface Rnd {

    /**
     * Создать экземпляр {@link Rnd}
     */
    static Rnd create() {
        return new RndImpl();
    }

    /**
     * Создать экземпляр {@link Rnd} с начальным значение генератора Random для получения
     * предсказуемых последовательностей.
     */
    static Rnd create(long seed) {
        return new RndImpl(seed);
    }

    //////

    String E_CHARS = "qwertyuiopasdfghjklzxcvbnm";
    String R_CHARS = "йцукенгшщзхъфывапролджэячсмитьбю";
    String N_CHARS = "0123456789";
    String ER_CHARS = E_CHARS + R_CHARS;
    String ERN_CHARS = E_CHARS + R_CHARS + N_CHARS;

    //////

    /**
     * Установить начальное значение генератора Random для получения
     * предсказуемых последовательностей.
     */
    void setSeed(long seed);

    /**
     * Текущий используемый random.
     */
    Random getRandom();

    /**
     * Установить другой random для использования
     */
    void setRandom(Random random);

    //////

    /**
     * true/false с вероятностью 50%
     */
    boolean bool();

    /**
     * true/false с вероятностью chanceTrue/chanceFalse
     */
    boolean bool(int chanceTrue, int chanceFalse);

    //////

    /**
     * int в указанном диапазоне
     */
    int num(int min, int max);

    /**
     * double в указанном диапазоне с указанным числом знаков после запятой (scale).
     */
    double doub(double min, double max, int scale);

    //////

    /**
     * Значение value или null с вероятностью chanceNull/chanceNotNull
     */
    Object nullValue(Object value, int chanceNull, int chanceNotNull);

    /**
     * Значение value или null с вероятностью 50%
     */
    Object nullValue(Object value);

    //////

    /**
     * Один из символов из chars
     */
    char choice(CharSequence chars);

    /**
     * Один из объектов из items
     */
    Object choice(List items);

    /**
     * Один из объектов из items
     */
    Object choice(Object[] items);

    //////

    /**
     * Текст
     *
     * @param chars    набор символов
     * @param min      минимальная длина
     * @param max      максимальная длина
     * @param wordSize примерный размер слова. Если 0 - то пробелы не вставляются.
     * @return текст
     */
    String text(String chars, int min, int max, int wordSize);

    //////

    /**
     * XDate в указанном диапазоне
     */
    XDate date(XDate min, XDate max);

    /**
     * XDateTime в указанном диапазоне
     */
    XDateTime datetime(XDateTime min, XDateTime max);

}
