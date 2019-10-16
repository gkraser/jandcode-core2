package jandcode.core.web.gsp;

/**
 * Интерфейс для объектов, которые желают рендерится в gsp.
 */
public interface IGspRender {

    /**
     * Отрендерить объект в указанный context.
     */
    void renderTo(GspContext gspContext) throws Exception;


}
