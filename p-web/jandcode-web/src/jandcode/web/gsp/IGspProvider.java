package jandcode.web.gsp;

import java.util.*;

/**
 * Провайдер для gsp.
 * Его цель - загрузить набор GspDef по его правилам.
 */
public interface IGspProvider {

    /**
     * Загрузить gsps.
     * Возвращает null, если нет доступных объектов для загрузки.
     */
    List<GspDef> loadGsps() throws Exception;

    /**
     * Создать GspDef по указанному имени.
     * Если имя неправильное, возвращает null.
     * Может генерировать ошибку, если имя правильное, но объект не может существовать.
     */
    GspDef resolveGsp(String gspName);

}
