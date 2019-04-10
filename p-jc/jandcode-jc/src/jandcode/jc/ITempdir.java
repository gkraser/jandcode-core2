package jandcode.jc;

/**
 * Временные каталоги
 */
public interface ITempdir {

    /**
     * Базовый каталог для временных файлов jc
     */
    String getTempdirRoot();

    /**
     * Возвращает путь до временного каталога localPath.
     * Учитывается версия jc и текущий рабочий каталог.
     */
    String getTempdir(String localPath);

    /**
     * Возвращает путь до временного каталога localPath,
     * который разделяется между всеми экземплярами jc.
     */
    String getTempdirCommon(String localPath);

}
