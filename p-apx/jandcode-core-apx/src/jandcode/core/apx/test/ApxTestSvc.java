package jandcode.core.apx.test;

import jandcode.commons.*;
import jandcode.core.dao.*;
import jandcode.core.test.*;
import jandcode.core.web.test.*;

import java.net.http.*;
import java.util.*;

/**
 * Расширение для тестов: поддержка apx
 */
public class ApxTestSvc extends BaseAppTestSvc {

    public WebTestSvc web;

    public void setUp() throws Exception {
        super.setUp();
        this.web = testSvc(WebTestSvc.class);
    }

    /**
     * Создать экземпляр dao.
     *
     * @param cls            для какого класса
     * @param daoInvokerName какой daoInvoker использовать.
     *                       Если не указано - 'default'. Можно например указать модельный 'model:default'
     */
    public <A> A createDao(Class<A> cls, String daoInvokerName) throws Exception {
        if (UtString.empty(daoInvokerName)) {
            daoInvokerName = "default";
        }
        DaoService svc = getApp().bean(DaoService.class);
        DaoInvoker di = svc.getDaoInvoker(daoInvokerName);
        return di.createDao(cls);
    }

    /**
     * Создать экземпляр dao для daoInvoker=default
     *
     * @param cls для какого класса
     */
    public <A> A createDao(Class<A> cls) throws Exception {
        return createDao(cls, null);
    }

    ////// json rpc

    /**
     * Выполнить метод json-rpc
     *
     * @param uri        на каком uri обработчик, например 'api'*
     * @param methodName имя метода
     * @param params     параметры, List для позиционных и map для поименнованных параметров
     * @return результат работы методы в виде map
     */
    public Map<String, Object> execJsonRpc(String uri, String methodName, Object params) {
        WebClientRequest req = web.createRequest();
        req.setUri(uri);

        WebClientResponse resp = req.exec(b -> {
            Map<String, Object> p = new LinkedHashMap<>();

            p.put("id", UUID.randomUUID().toString());
            p.put("method", "dao1/str1");
            p.put("params", params);

            String bodyRequest = UtJson.toJson(p);
            b.POST(HttpRequest.BodyPublishers.ofString(bodyRequest));
        });

        String s = resp.getBodyText();

        return (Map<String, Object>) UtJson.fromJson(s);
    }

}
