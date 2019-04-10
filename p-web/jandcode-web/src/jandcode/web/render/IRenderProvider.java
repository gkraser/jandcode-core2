package jandcode.web.render;

import java.util.*;

/**
 * Провайдер для render.
 * Его цель - загрузить набор RenderDef по его правилам.
 */
public interface IRenderProvider {

    /**
     * Загрузить renders.
     * Возвращает null, если нет доступных объектов для загрузки.
     */
    List<RenderDef> loadRenders() throws Exception;

}
