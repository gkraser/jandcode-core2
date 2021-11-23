package jandcode.core.apx.test;

import jandcode.commons.*;
import jandcode.core.dbm.test.*;
import jandcode.core.test.*;
import jandcode.core.web.test.*;

import java.net.http.*;
import java.util.*;

/**
 * Расширение для тестов: поддержка apx
 */
public class ApxTestSvc extends BaseAppTestSvc {

    public WebTestSvc web;
    public DbmTestSvc dbm;

    public void setUp() throws Exception {
        super.setUp();
        this.web = testSvc(WebTestSvc.class);
        this.dbm = testSvc(DbmTestSvc.class);
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
