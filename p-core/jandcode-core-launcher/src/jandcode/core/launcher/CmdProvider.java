package jandcode.core.launcher;

import java.util.*;

/**
 * Провайдер для cmd.
 * Его цель - загрузить набор Cmd по его правилам.
 */
public interface CmdProvider {

    /**
     * Загрузить actions.
     * Возвращает null или пустой список, если нет доступных объектов для загрузки.
     */
    Collection<Cmd> loadCmds() throws Exception;

}
