package jandcode.core.web.virtfile;

import java.util.*;

/**
 * Провайдер для mount.
 * Его цель - загрузить набор Mount по его правилам.
 */
public interface IMountProvider {

    /**
     * Загрузить mounts.
     * Возвращает null, если нет доступных объектов для загрузки.
     */
    List<Mount> loadMounts() throws Exception;

}
