package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.dbm.verdb.*;

import java.util.*;

public class VerdbFileImpl extends BaseVerdbItem implements VerdbFile {

    private VerdbDir dir;
    private List<VerdbOper> opers;

    public VerdbFileImpl(VerdbDir dir, String path, long versionNum1, long versionNum2) {
        setPath(path);
        this.dir = dir;
        this.setVersion(VerdbVersion.create(versionNum1, versionNum2, 0));
    }

    public VerdbDir getDir() {
        return dir;
    }

    public List<VerdbOper> getOpers() {
        if (opers == null) {
            synchronized (this) {
                if (opers == null) {
                    opers = loadOpers();
                }
            }
        }
        return opers;
    }

    private List<VerdbOper> loadOpers() {
        String ext = UtFile.ext(getPath());
        try {
            if (ext.equals("sql")) {
                return loadOpers_sql();
            } else if (ext.equals("groovy")) {
                return loadOpers_groovy();
            } else {
                throw new XError("Не поддерживаемый тип файла [{0}]", ext);
            }
        } catch (Exception e) {
            throw new XErrorMark(e, getPath());
        }
    }

    private List<VerdbOper> loadOpers_sql() throws Exception {
        List<VerdbOper> res = new ArrayList<>();
        String script = UtFile.loadString(getPath());
        List<String> lst = UtSql.splitScript(script);
        int num = 1;
        for (String s : lst) {
            VerdbOper_sql oper = new VerdbOper_sql(this, num, s);
            res.add(oper);
        }
        if (res.size() == 0) {
            res.add(new VerdbOper_dummy(this, 1));
        }
        return res;
    }

    private List<VerdbOper> loadOpers_groovy() throws Exception {
        List<VerdbOper> res = new ArrayList<>();
        String script = UtFile.loadString(getPath());
        res.add(new VerdbOper_groovy(this, 1, script));
        return res;
    }

    public VerdbVersion getLastVersion() {
        if (getOpers().size() == 0) {
            return getVersion();
        }
        return getOpers().get(getOpers().size() - 1).getVersion();
    }
}
