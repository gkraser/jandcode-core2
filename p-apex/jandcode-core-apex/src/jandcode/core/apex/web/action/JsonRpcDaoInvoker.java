package jandcode.core.apex.web.action;

import com.google.gson.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.dao.*;
import jandcode.core.web.action.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Выполнение dao через протокол json-rpc.
 * Используется в action, которая представляет собой entry-point для dao-api.
 */
public class JsonRpcDaoInvoker {

    private ActionRequestUtils req;
    private String daoHolderName;
    private String id;
    private String method;
    private DaoHolderItem dhItem;

    public JsonRpcDaoInvoker(ActionRequestUtils req, String daoHolderName) {
        this.req = req;
        this.daoHolderName = daoHolderName;
    }

    public void invokeDao() {
        try {
            // разбираем параметры из тела запроса
            JsonElement po = UtJson.getGson().fromJson(req.getHttpRequest().getReader(), JsonElement.class);
            if (!po.isJsonObject()) {
                throw new XError("Тело запроса должно быть json-объектом");
            }

            JsonObject pm = po.getAsJsonObject();
            if (pm.has("id")) {
                this.id = UtCnv.toString(pm.get("id").getAsString());
            }

            method = null;
            if (pm.has("method")) {
                method = UtCnv.toString(pm.get("method").getAsString());
            }
            if (UtString.empty(method)) {
                throw new XError("method не указан");
            }

            JsonElement params = pm.get("params");
            JsonArray listParams;
            if (params == null) {
                listParams = new JsonArray();
            } else if (params.isJsonArray()) {
                listParams = params.getAsJsonArray();
            } else {
                listParams = new JsonArray();
                listParams.add(params);
            }

            // ну все, готовы к выполнению

            // конвертируем параметры

            DaoHolder daoHolder = req.getApp().bean(DaoService.class).getDaoHolder(this.daoHolderName);
            dhItem = daoHolder.getItems().get(method);

            Method javaMethod = dhItem.getMethodDef().getMethod();
            int javaMethoodPrmCnt = javaMethod.getParameterCount();
            if (javaMethoodPrmCnt != listParams.size()) {
                throw new XError("Число параметров метода ({0}) не совпадает с " +
                        "числом переданных переметров ({1})",
                        javaMethoodPrmCnt,
                        listParams.size());
            }

            Class<?>[] javaMethodParams = javaMethod.getParameterTypes();
            Object[] args = new Object[javaMethoodPrmCnt];
            for (int i = 0; i < javaMethoodPrmCnt; i++) {
                try {
                    args[i] = UtJson.getGson().fromJson(listParams.get(i), javaMethodParams[i]);
                } catch (JsonSyntaxException e) {
                    throw new XErrorMark(e, "параметр " + i);
                }
            }

            // вызываем

            Object result = daoHolder.invokeDao(method, args);

            // формируем ответ

            Map<String, Object> resultMap = new LinkedHashMap<>();
            if (this.id != null) {
                resultMap.put("id", id);
            }
            resultMap.put("result", result);

            markResult(resultMap);

            req.render(resultMap);

        } catch (Throwable e) {
            renderError(e);
        }
    }

    protected void renderError(Throwable e) {
        req.setException(e);
        req.getHttpResponse().setStatus(500);
        //
        Map<String, Object> err = new LinkedHashMap<>();
        ErrorInfo ei = UtError.createErrorInfo(e);
        err.put("message", ei.getText());

        //
        Map<String, Object> res = new LinkedHashMap<>();
        if (this.id != null) {
            res.put("id", id);
        }
        markResult(res);
        res.put("error", err);

        //
        req.render(res);
    }

    protected void markResult(Map res) {
        if (req.getApp().getEnv().isDev()) {
            if (method != null) {
                res.put("$method", method);
            }
            if (dhItem != null) {
                res.put("$javaMethod", dhItem.getMethodDef().getMethod().toString());
            }
        }
    }

}
