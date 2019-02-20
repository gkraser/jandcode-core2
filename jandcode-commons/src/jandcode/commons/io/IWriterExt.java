package jandcode.commons.io;

/**
 * Расширение для Writer с указанием имени файла и кодировки
 */
public interface IWriterExt {

    /**
     * Условное имя файла. Если пустая строка - значит запись в безымянный источник,
     * например строку.
     */
    String getFilename();

    /**
     * Кодировка
     */
    String getCharset();

}
