package jandcode.core.apx.jsonrpc;

import com.google.gson.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.web.action.*;

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
    private DaoMethodDef methodDef;

    public JsonRpcDaoInvoker(ActionRequestUtils req, String daoHolderName) {
        this.req = req;
        this.daoHolderName = daoHolderName;
    }

    public void invokeDao() {
        try {
            // разбираем параметры из тела запроса
            JsonElement po = UtJson.getGson().fromJson(req.getHttpRequest().getReader(), JsonElement.class);
            if (po == null || !po.isJsonObject()) {
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
            JsonArray listParams = null;
            JsonObject objectParams = null;
            if (params == null) {
                throw new XError("params не указан");
            } else if (params.isJsonArray()) {
                listParams = params.getAsJsonArray();
            } else if (params.isJsonObject()) {
                objectParams = params.getAsJsonObject();
            } else {
                throw new XError("params должен быть Array или Object");
            }

            // ну все, готовы к выполнению

            // конвертируем параметры

            DaoHolder daoHolder = req.getApp().bean(DaoService.class).getDaoHolder(this.daoHolderName);
            methodDef = daoHolder.getItems().get(method).getMethodDef();

            int paramsCount = methodDef.getParams().size();
            Object[] args = new Object[paramsCount];

            if (listParams != null) {
                if (paramsCount != listParams.size()) {
                    throw new XError("Число параметров метода ({0}) не совпадает с " +
                            "числом переданных параметров ({1})",
                            paramsCount,
                            listParams.size());
                }
                for (int i = 0; i < paramsCount; i++) {
                    try {
                        args[i] = UtJson.getGson().fromJson(listParams.get(i), methodDef.getParams().get(i).getType());
                    } catch (JsonSyntaxException e) {
                        throw new XErrorMark(e, "параметр " + i);
                    }
                }
            } else if (objectParams != null) {
                var keys = objectParams.keySet();
                for (var p : methodDef.getParams()) {
                    if (keys.contains(p.getName())) {
                        args[p.getIndex()] = UtJson.getGson().fromJson(objectParams.get(p.getName()), p.getType());
                    } else {
                        throw new XError("Отсуствует параметр {0}", p.getName());
                    }
                }
            } else {
                throw new XError("unknown error");
            }

            // вызываем

            DaoContext daoContext = daoHolder.invokeDao(null, method, args);
            Object result = wrapResult(daoContext);

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
            if (methodDef != null) {
                res.put("$javaClass", methodDef.getClassDef().getCls().getName());
                res.put("$javaMethodName", methodDef.getMethod().getName());
                res.put("$javaMethod", methodDef.getMethod().toString());
            }
        }
    }

    protected Object wrapResult(DaoContext daoContext) throws Exception {
        Object result = daoContext.getResult();
        Model model = null;
        BeanDef b = daoContext.getBeanFactory().findBean(Model.class, false);
        if (b != null) {
            model = (Model) b.getInst();
        }
        if (model != null) {

            // есть модель. Нужно кое-что с результатом сделать
            DictService dictService = model.bean(DictService.class);
            dictService.resolveDicts(result);
        }
        return result;
    }

}
