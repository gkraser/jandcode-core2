package jandcode.core.dbm.ddl.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.simxml.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;
import jandcode.core.dbm.mdb.*;

import java.io.*;
import java.util.*;

public class DDLScriptImpl extends BaseModelMember implements DDLScript {

    private List<DDLOper> items = new ArrayList<>();

    class DDLScriptLoader implements ILoader {

        public void loadFrom(Reader reader) throws Exception {
            SimXml x = new SimXmlNode();
            SimXmlLoader ldr = new SimXmlLoader(x);
            ldr.setTrimSpaceEnd(true);
            ldr.loadFrom(reader);
            DDLScriptImpl.this.loadFromXml(x);
        }

        public LoadFrom load() {
            return new LoadFrom(this);
        }
    }

    class DDLScriptSaver implements ISaver {

        public void saveTo(Writer writer) throws Exception {
            SimXml x = new SimXmlNode();
            DDLScriptImpl.this.saveToXml(x);
            SimXmlSaver svr = new SimXmlSaver(x);
            svr.saveTo(writer);
        }

        public SaveTo save() {
            return new SaveTo(this);
        }
    }

    public List<DDLOper> getItems() {
        return items;
    }

    public void exec(Mdb mdb) throws Exception {
        for (DDLOper op : getItems()) {
            op.exec(mdb);
        }
    }

    public void saveToXml(SimXml x) throws Exception {
        for (DDLOper op : items) {
            SimXml x1 = x.addChild(DDLConsts.XML_TAG_DDL);
            op.saveToXml(x1);
        }
    }

    public void loadFromXml(SimXml x) throws Exception {
        items.clear();

        DDLService svc = getModel().bean(DDLService.class);

        for (SimXml x1 : x.getChilds()) {
            if (!x1.hasName(DDLConsts.XML_TAG_DDL)) {
                continue; // не наш узел, возможно безымянный мусор
            }
            String type = x1.getAttrs().getString("type");
            DDLOper op = svc.createOperInst(type);
            op.loadFromXml(x1);
            items.add(op);
        }

    }

    public LoadFrom load() {
        return new LoadFrom(new DDLScriptLoader());
    }

    public SaveTo save() {
        return new SaveTo(new DDLScriptSaver());
    }

    public String getSqlScript() {
        StringBuilder sb = new StringBuilder();
        for (DDLOper op : items) {
            sb.append(op.getSqlScript());
            sb.append("\n");
            sb.append(UtSql.SCRIPT_DELIMITER);
            sb.append("\n");
        }
        return sb.toString();
    }

}
