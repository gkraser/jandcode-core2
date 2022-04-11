package jandcode.core.dbm.ddl.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;

import java.util.*;

public abstract class BaseDDLProvider extends BaseModelMember implements DDLProvider {

    private Conf conf;
    private Set<DDLStage> stages = new HashSet<>();
    private Object context;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
        //
        if (this.stages.size() == 0) {
            this.stages.add(DDLStage.afterTables);
        }
    }

    public List<DDLOper> loadOpers(DDLStage stage) throws Exception {
        if (this.getStages().contains(stage)) {
            List<DDLOper> res = new ArrayList<>();
            onLoad(res, stage);
            return res;
        } else {
            return null;
        }
    }

    /**
     * Перекрывается в потомках для реализации
     *
     * @param res   куда записывать
     * @param stage какая сейчас стадия
     */
    protected abstract void onLoad(List<DDLOper> res, DDLStage stage) throws Exception;

    //////

    public Conf getConf() {
        return conf;
    }

    public Set<DDLStage> getStages() {
        return stages;
    }

    /**
     * Установить стадии. Можно указывать несколько значений через ',' или
     * '*' для всех стадий
     */
    public void setStage(String s) {
        this.stages.clear();
        if (UtString.empty(s)) {
            return;
        }
        String[] a = s.split(",");
        for (String b : a) {
            if (b.equals("*")) {
                this.stages.addAll(DDLStage.getStages());
            } else {
                this.stages.add(DDLStage.fromString(b));
            }
        }
    }

    /**
     * Базовое имя по умолчанию для генерированных {@link DDLOper}.
     */
    protected String getBaseName() {
        return getConf().getName();
    }

    /**
     * В контексте какого объекта получать операторы.
     * Опционально. Зависит от ситуации. Например, если ddl собираются из
     * домена и генерируются, то в качестве контекста можно передать домен.
     */
    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

}
