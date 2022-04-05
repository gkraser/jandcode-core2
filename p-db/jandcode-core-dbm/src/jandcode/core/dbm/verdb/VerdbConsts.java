package jandcode.core.dbm.verdb;

/**
 * Константы для verdb
 */
public class VerdbConsts {

    /**
     * Имя таблицы с информацией о версиях в базе
     */
    public static final String TABLE_VERDB_INFO = "verdb_info";

    /**
     * Список расширений поддерживаемых файлов
     */
    public static final String[] SUPPORTED_FILES = new String[]{"sql", "groovy"};


    /**
     * Поддерживается ли файл с указанным расширением
     *
     * @param ext расширение файла
     */
    public static boolean isSupportedFile(String ext) {
        for (String s : SUPPORTED_FILES) {
            if (s.equals(ext)) {
                return true;
            }
        }
        return false;
    }
}
