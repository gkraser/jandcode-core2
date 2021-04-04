package jandcode.commons.cli;

import java.util.*;

/**
 * Парзер командной строки
 */
public interface CliParser {

    /**
     * Текущие аргументы
     */
    List<String> getArgs();

    /**
     * Извлечь первый параметр.
     * Параметр удаляется из {@link CliParser#getArgs()}.
     *
     * @return null, если нет параметров
     */
    String extractParam();

    /**
     * Извлечь значения опций по их описанию
     *
     * @param opts описания опций
     * @return значения опций
     */
    Map<String, Object> extractOpts(Collection<? extends ICliOpt> opts);

    /**
     * Извлечь опции по описанию
     *
     * @param desc описания опций
     * @return значения опций
     */
    Map<String, Object> extractOpts(ICliDef desc);

}
