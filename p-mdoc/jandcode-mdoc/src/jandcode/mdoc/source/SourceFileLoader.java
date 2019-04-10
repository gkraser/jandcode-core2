package jandcode.mdoc.source;

import java.util.*;

/**
 * Загрузчик файлов
 */
public interface SourceFileLoader {

    /**
     * Загрузить список файлов
     *
     * @return null или пустой список, если нет у него файлов
     */
    List<SourceFile> loadFiles() throws Exception;

}
