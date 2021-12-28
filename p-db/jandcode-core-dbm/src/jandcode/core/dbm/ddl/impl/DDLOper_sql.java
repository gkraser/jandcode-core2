package jandcode.core.dbm.ddl.impl;

import jandcode.commons.*;
import jandcode.commons.simxml.*;
import jandcode.core.dbm.mdb.*;

/**
 * ddl в виде sql
 */
public class DDLOper_sql extends BaseDDLOper {

    private String sqlText;

    public String getType() {
        return "sql";
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public void exec(Mdb mdb) throws Exception {
        mdb.execScript(getSqlText());
    }

    protected void onSaveToXml(SimXml x) throws Exception {
        x.setText(getSqlText());
    }

    protected void onLoadFromXml(SimXml x) throws Exception {
        setSqlText(UtString.normalizeIndent(x.getText()));
    }

    public String getSqlScript() {
        return DDLUtils.PREFIX_DDL_NAME + " " + getName() + "\n" + getSqlText();
    }

}
