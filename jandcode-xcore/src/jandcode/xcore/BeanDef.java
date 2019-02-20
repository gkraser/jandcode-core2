package jandcode.xcore;

import jandcode.commons.named.*;

/**
 * Определение бина.
 */
public interface BeanDef extends INamed {

    /**
     * Класс бина
     */
    Class getCls();

    /**
     * Получить экземпляр бина
     */
    Object getInst();

    /**
     * Создать новый экземпляр экземпляр бина
     */
    Object createInst();

    /**
     * Есть ли созданный экземпляр для этого бина.
     * Если false, значит бин зарегистрирован, но еще не разу не создавался его
     * экземпляр.
     */
    boolean hasInst();

    /**
     * true - бин является прототипом.
     * Такие бины нельзя получить через getInst(), их можно только создать
     * через createInst()
     */
    boolean isPrototype();

    /**
     * true - бин зарегистрирован в режиме автосоздания.
     * Экземпляр создается при его регистрации из conf.
     */
    boolean isAutoCreate();

}
