package jandcode.core.dbm.verdb;

import jandcode.core.dbm.verdb.impl.*;

/**
 * Версия для verdb.
 * Состоит из 3-частей: каталог, файл, оператор.
 * Разделитель - точка. Вче части - целые положительный числа long.
 */
public interface VerdbVersion extends Comparable<VerdbVersion> {

    /**
     * Создать экземпляр из строки
     */
    static VerdbVersion create(String ver) {
        return new VerdbVersionImpl(ver);
    }

    /**
     * Создать экземпляр из указанных версий
     */
    static VerdbVersion create(long v1, long v2, long v3) {
        return new VerdbVersionImpl(v1, v2, v3);
    }

    /**
     * Версия в виде строки
     */
    String getText();

    /**
     * Первая часть версии (каталог)
     */
    long getV1();

    /**
     * Вторая часть версии (файл)
     */
    long getV2();

    /**
     * Вторая часть версии (оператор)
     */
    long getV3();

    /**
     * Создает новый экземпляр версии. Если параметр <0, то в результат попадет текщий,
     * иначе - новый
     */
    VerdbVersion with(long v1, long v2, long v3);

}