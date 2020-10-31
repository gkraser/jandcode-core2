package jandcode.core.apex.web.action;

import jandcode.core.web.action.*;

/**
 * Вызов dao через json rpc
 */
public class DaoAction extends BaseAction {

    /**
     * Выполнение dao.
     * dao берется из daoHolder default.
     */
    public void invokeDao() throws Exception {
        JsonRpcDaoInvoker inv = new JsonRpcDaoInvoker(getReq(), "default");
        inv.invokeDao();
    }

}
