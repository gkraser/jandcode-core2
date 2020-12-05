package jandcode.core.dbm.dao.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import jandcode.core.dao.impl.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;

public class ModelDaoServiceImpl extends BaseModelMember implements ModelDaoService {

    private DaoInvoker daoInvoker;
    private DaoHolder daoHolder;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf modelCof = getModel().getConf();
        this.daoInvoker = (DaoInvoker) getModel().create(modelCof.getConf("dao/invoker/default"));
        this.daoHolder = getModel().create(modelCof.getConf("dao/holder/default"), DaoHolderImpl.class);
    }

    public DaoInvoker getDaoInvoker() {
        return daoInvoker;
    }

    public DaoHolder getDaoHolder() {
        return daoHolder;
    }
}
