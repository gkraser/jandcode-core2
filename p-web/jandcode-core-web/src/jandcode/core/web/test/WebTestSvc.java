package jandcode.core.web.test;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.test.*;
import jandcode.core.test.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.core.web.action.impl.*;
import jandcode.core.web.gsp.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * Расширение web-тестов
 */
public class WebTestSvc extends BaseTestSvc {

    private static WebServerHolder webServerHolder = new WebServerHolder();

    protected UtilsTestSvc utils;

    static class TmpAction extends CustomActionDef {

        IAction action;

        public TmpAction(IAction action) {
            setName(UUID.randomUUID().toString());
            this.action = action;
        }

        public IAction createInst() {
            return action;
        }
    }


    public void setUp() throws Exception {
        super.setUp();
        this.utils = testSvc(UtilsTestSvc.class);
    }

    /**
     * Приложение
     */
    public AppTestSvc getApp() {
        return testSvc(AppTestSvc.class);
    }

    /**
     * WebService
     */
    public WebService getWebService() {
        return getApp().bean(WebService.class);
    }

    //////

    /**
     * Выполнить action в контексте исполнения запроса на сервере.
     * Создается временная action, которая выполняется как переменный обработчик.
     *
     * @param action что выполнить
     */
    public WebClientResponse execAction(IAction action) {
        NamedList<ActionDef> actions = getWebService().getActions();
        ActionDef tmpAction = new TmpAction(action);
        actions.add(tmpAction);
        try {
            WebClientRequest req = getWebServer().createRequest();
            req.setUri(tmpAction.getName());
            try {
                return req.exec();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        } finally {
            actions.remove(tmpAction);
        }
    }

    /**
     * Выполнить запрос и вернуть результат как строку
     */
    public String execRequest(String uri, Map<String, String> queryParams) throws Exception {
        WebClientRequest req = getWebServer().createRequest();
        req.setUri(uri);
        if (queryParams != null) {
            req.getQueryParams().putAll(queryParams);
        }
        WebClientResponse resp = req.exec();
        return resp.getBodyText();
    }

    /**
     * Выполнить запрос и вернуть результат как строку
     */
    public String execRequest(String uri) throws Exception {
        return execRequest(uri, null);
    }

    /**
     * Отрендерить указанный gsp и возвратить полученный текст
     *
     * @param gspName имя gsp
     * @param args    аргументы. Может быть null
     */
    public String renderGsp(String gspName, Map args) throws Exception {
        AtomicReference<String> res = new AtomicReference<>();
        execAction((request) -> {
            GspContext ctx = getWebService().createGspContext();
            ITextBuffer buf = ctx.render(gspName, args);
            res.set(buf.toString());
        });
        return res.get();
    }

    /**
     * Отрендерить указанный gsp и возвратить полученный текст
     *
     * @param gspName имя gsp
     */
    public String renderGsp(String gspName) throws Exception {
        return renderGsp(gspName, null);
    }

    ////// webserver

    /**
     * Возвращает webserver, запущенный для текущего приложения.
     * Запуск осуществляется при первом обращении к методу.
     */
    public WebServer getWebServer() {
        return webServerHolder.getWebServer(getApp());
    }

}
