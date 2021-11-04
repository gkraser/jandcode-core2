package jandcode.core.apx.jsonrpc;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.web.action.*;

/**
 * Вызов dao через json rpc
 */
public class JsonRpcAction extends BaseAction {

    private String daoHolderName;

    /**
     * Выполнение dao.
     * dao берется из {@link JsonRpcAction#getDaoHolderName()}.
     */
    public void index() throws Exception {
        JsonRpcDaoInvoker inv = new JsonRpcDaoInvoker(getReq(), getDaoHolderName());
        inv.invokeDao();
    }

    //////

    public String getDaoHolderName() {
        if (UtString.empty(daoHolderName)) {
            throw new XError("daoHolder not assigned");
        }
        return daoHolderName;
    }

    public void setDaoHolderName(String daoHolderName) {
        this.daoHolderName = daoHolderName;
    }
}
