package jandcode.core.web.std.action;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.web.action.*;

/**
 * Специальная action, которая просто рендерит gsp.
 * Она используется в случае, когда в conf для action не указан класс, но
 * указан атрибут template.
 * <p>
 * Может использоваться как базовый класс для других похожих случаев обработки.
 */
public class GspAction extends BaseAction {

    private String template;

    /**
     * Шаблон gsp, который будет отрендерен.
     */
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Возвращает шаблон, который нужно отрендерить.
     * По умолчанию просто проверяет наличие значения в свойстве template
     * и возвращает его.
     */
    protected String resolveTemplate() {
        String template = getTemplate();
        if (UtString.empty(template)) {
            throw new XError("Не задан параметр template для action [{0}]", getName());
        }
        return template;
    }

    //////

    protected void onExec() throws Exception {
        getReq().renderGsp(resolveTemplate());
    }

}
