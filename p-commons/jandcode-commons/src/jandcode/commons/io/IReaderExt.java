package jandcode.commons.io;

import org.xml.sax.*;

/**
 * Расширение для Reader с указанием имени файла и кодировки
 */
public interface IReaderExt {

    /**
     * Условное имя файла. Если пустая строка - значит чтение из безымянного источника,
     * например строки.
     */
    String getFilename();

    /**
     * Кодировка
     */
    String getCharset();

    /**
     * InputSource. Возможно для Reader, возможно для InputStream, по обстоятельствам.
     */
    InputSource getInputSource();

}
