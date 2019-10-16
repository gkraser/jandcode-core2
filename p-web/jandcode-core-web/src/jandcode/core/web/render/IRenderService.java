package jandcode.core.web.render;

import jandcode.commons.named.*;

/**
 * Интерфейс сервиса для хранения и управление render
 */
public interface IRenderService extends IRenderFactory {

    /**
     * Зарегистрированные render.
     */
    NamedList<RenderDef> getRenders();

    /**
     * Создать render по имени. Если не найден, генерируется ошибка.
     */
    IRender createRender(String name);

}
