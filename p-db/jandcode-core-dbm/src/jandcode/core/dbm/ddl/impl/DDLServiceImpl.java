package jandcode.core.dbm.ddl.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;

public class DDLServiceImpl extends BaseModelMember implements DDLService {

    private NamedList<DDLTypeInfo> ddlTypes = new DefaultNamedList<>("Не найден ddl-type [{0}]");

    class DDLTypeInfo extends Named {

        Class cls;

        public DDLTypeInfo(String name, Class cls) {
            setName(name);
            this.cls = cls;
        }

        public DDLOper createInst() {
            return (DDLOper) getModel().create(cls);
        }

    }

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf modelCof = getModel().getConf();

        //
        for (Conf x : modelCof.getConfs("ddl-type")) {
            DDLTypeInfo z = new DDLTypeInfo(x.getName(), UtClass.getClass(x.getString("class")));
            ddlTypes.add(z);
        }

    }

    public DDLOper createOperInst(String type) {
        return ddlTypes.get(type).createInst();
    }

}
