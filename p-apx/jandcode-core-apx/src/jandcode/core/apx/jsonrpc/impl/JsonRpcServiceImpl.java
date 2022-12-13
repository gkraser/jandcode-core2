package jandcode.core.apx.jsonrpc.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.apx.jsonrpc.*;
import jandcode.core.dao.*;

public class JsonRpcServiceImpl extends BaseComp implements JsonRpcService {

    private ClassLinks<TypeDef> typesByClass = new ClassLinks<>();

    class TypeDef {
        Conf conf;
        Class cls;
        Class clientConvertorCls;

        public TypeDef(Conf conf) {
            this.conf = conf;
            this.cls = UtClass.getClass(conf.getName());
            String s = conf.getString("client-convertor");
            if (!UtString.empty(s)) {
                this.clientConvertorCls = UtClass.getClass(s);
            }
        }

        public Class getCls() {
            return cls;
        }

        public Class getClientConvertorCls() {
            return clientConvertorCls;
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        for (Conf x : getApp().getConf().getConfs("apx/jsonrpc/type")) {
            try {
                TypeDef td = new TypeDef(x);
                typesByClass.add(td.getCls(), td);
            } catch (Exception e) {
                throw new XErrorMark(e, x.origin().toString());
            }
        }
    }

    public Object convertForClient(DaoContext daoContext, Object inst) {
        if (inst == null) {
            return inst;
        }
        TypeDef td = this.typesByClass.get(inst.getClass());
        if (td == null) {
            return inst;
        }
        Class clsConvertor = td.getClientConvertorCls();
        if (clsConvertor == null) {
            return inst;
        }
        JsonRpcClientConvertor cnv = (JsonRpcClientConvertor) getApp().create(clsConvertor);
        return cnv.convert(daoContext, inst);
    }

}
