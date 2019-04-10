package jandcode.web.virtfile;


import jandcode.core.*;

/**
 * Описание типа файла по расширению.
 * Имя объекта - это расширение файла.
 */
public interface FileType extends Comp {

    /**
     * Текстовый файл
     */
    String TYPE_TEXT = "text";

    /**
     * Бинарный файл
     */
    String TYPE_BIN = "bin";

    /**
     * Приватный файл. Такие файлы не доступны как ресурсы
     */
    String TYPE_PRIVATE = "private";

    //////

    /**
     * mime
     */
    String getMime();

    /**
     * Тип (bin, text, private). См. константы TYPE_xxx.
     */
    String getType();

    /**
     * true - этот тип файла является шаблоном.
     */
    boolean isTml();

    /**
     * Файл имеет тип private
     */
    boolean isPrivate();

    /**
     * Файл имеет тип bin
     */
    boolean isBin();

    /**
     * Файл имеет тип text
     */
    boolean isText();

}
