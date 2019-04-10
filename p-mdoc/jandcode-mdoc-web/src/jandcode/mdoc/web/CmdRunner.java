package jandcode.mdoc.web;

/**
 * Интерфейс для запуска внешних вещей
 */
public interface CmdRunner {

    /**
     * Запустить редактор для указанного файла
     *
     * @param file полное имя файла
     */
    void runEditor(String file, int lineNumber) throws Exception;

}
