package jandcode.commons.datetime;

/**
 * Разобранная на запчасти дата
 */
public interface XDateTimeDecoded {

    int getYear();

    int getMonth();

    int getDay();

    int getHour();

    int getMin();

    int getSec();

    int getMsec();

    /**
     * День недели. 1-понедельник, 7-воскресенье
     */
    int getDow();

    /**
     * Есть ли время
     */
    boolean hasTime();

}
