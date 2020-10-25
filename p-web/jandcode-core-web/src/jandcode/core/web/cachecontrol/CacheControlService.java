package jandcode.core.web.cachecontrol;

import java.util.*;

/**
 * Сервис для информации о Cache-Control для разных ресурсов
 */
public interface CacheControlService {

    /**
     * Список правил для Cache-Control
     */
    List<CacheControl> getCacheControls();

    /**
     * Найти правило Cache-Control для указанного файла.
     *
     * @return null, если правило не задано
     */
    CacheControl findCacheControl(String path);

}
