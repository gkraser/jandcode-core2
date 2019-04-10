package jandcode.commons.io;

/**
 * callback для процесса сканирования.
 */
public interface DirScannerCallback<TYPE> {

    /**
     * Выполняется для каждого найденного файла (или каталога).
     *
     * @param file найденный элемент
     */
    void nextFile(TYPE file);


}
