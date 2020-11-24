package jandcode.core.apx.test;

import jandcode.commons.*;
import jandcode.core.dao.*;
import jandcode.core.test.*;

/**
 * Расширение для тестов: поддержка apx
 */
public class ApxTestSvc extends BaseAppTestSvc {

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

}
