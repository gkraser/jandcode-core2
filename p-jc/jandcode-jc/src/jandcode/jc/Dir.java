package jandcode.jc;

/**
 * Каталог
 */
public interface Dir {

    /**
     * Путь до каталога
     */
    String getPath();

    /**
     * Объеденить с указанным путем и возвратить результат как абсолютный путь
     */
    String join(CharSequence path);

    /**
     * Вызов join
     */
    String call(CharSequence path);

    /**
     * Имя каталога
     */
    String getName();

    /**
     * Новый экземпляр Dir
     *
     * @param path относительный или абсолютный
     */
    Dir dir(String path);

}