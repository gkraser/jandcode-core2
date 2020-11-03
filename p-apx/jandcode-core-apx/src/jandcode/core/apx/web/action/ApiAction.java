package jandcode.core.apx.web.action;

import jandcode.core.apx.web.utils.*;
import jandcode.core.web.action.*;

/**
 * Вызов dao через json rpc
 */
public class ApiAction extends BaseAction {

    private String daoHolderName = "api";

    /**
     * Выполнение dao.
     * dao берется из {@link ApiAction#getDaoHolderName()}.
     */
    public void index() throws Exception {
        JsonRpcDaoInvoker inv = new JsonRpcDaoInvoker(getReq(), getDaoHolderName());
        inv.invokeDao();
    }

    //////

    public String getDaoHolderName() {
        return daoHolderName;
    }

    public void setDaoHolderName(String daoHolderName) {
        this.daoHolderName = daoHolderName;
    }
}
