package jandcode.core.apex.web.action;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.web.action.*;

/**
 * action для запуска web-приложения apex.
 */
public class ApexWebappAction extends BaseAction {

    private String template;

    /**
     * Шаблон gsp, который представляет собой приложение.
     * Должен быть задан при регистрации action в module.cfx.
     */
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    //////

    protected void onExec() throws Exception {
        String template = getTemplate();
        if (UtString.empty(template)) {
            throw new XError("Не задан параметр template для action [{0}]", getName());
        }
        getReq().renderGsp(getTemplate());
    }

}
