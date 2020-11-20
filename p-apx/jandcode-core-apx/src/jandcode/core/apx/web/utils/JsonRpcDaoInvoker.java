package jandcode.core.apx.web.utils;

import com.google.gson.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.apx.web.utils.impl.*;
import jandcode.core.dao.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.std.*;
import jandcode.core.store.*;
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
            if (dhItem != null) {
                res.put("$javaClass", dhItem.getMethodDef().getCls().getName());
                res.put("$javaMethodName", dhItem.getMethodDef().getMethod().getName());
                res.put("$javaMethod", dhItem.getMethodDef().getMethod().toString());
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

            // 
            result = wrapResult(daoContext, result, model);
        }
        return result;
    }

    protected Object wrapResult(DaoContext daoContext, Object result, Model model) throws Exception {
        if (result instanceof Store) {
            return new JsonModelStoreWrapper((Store) result);

        } else if (result instanceof StoreRecord) {
            return new JsonModelStoreRecordWrapper((StoreRecord) result);

        } else if (result instanceof DataBox) {
            DataBox b = (DataBox) result;
            for (String key : b.keySet()) {
                b.put(key, wrapResult(daoContext, b.get(key), model));
            }
            return b;

        }
        return result;
    }

}
