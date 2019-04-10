package jandcode.jc;

/**
 * Манипулирование classpath
 */
public interface IClasspath {

    /**
     * Добавить библиотеки в classpath.
     * Так же добавляются и все зависимости.
     *
     * @param libs имя библиотеки, список библиотек или библиотека
     */
    void classpath(Object libs);


}
