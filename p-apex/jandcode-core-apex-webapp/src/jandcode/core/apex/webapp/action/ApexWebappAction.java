package jandcode.core.apex.webapp.action;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.web.action.*;

/**
 * action для запуска web-приложения apex.
 */
public class ApexWebappAction extends BaseAction {

    private String mainGsp;

    /**
     * main.gsp файл, который представляет собой приложение.
     * Должен быть задан при регистрации action в module.cfx.
     */
    public String getMainGsp() {
        return mainGsp;
    }

    public void setMainGsp(String mainGsp) {
        this.mainGsp = mainGsp;
    }

    //////

    protected void onExec() throws Exception {
        String mainGsp = getMainGsp();
        if (UtString.empty(mainGsp)) {
            throw new XError("Не задан параметр mainGsp для action [{0}]", getName());
        }
        getReq().renderGsp(getMainGsp());
    }

}
