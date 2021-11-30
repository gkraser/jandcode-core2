package jandcode.core.dbm.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;

import java.util.*;

/**
 * Описание модели, полученное из conf
 */
public class ModelDefImpl extends BaseComp implements ModelDef {

    private Conf conf;
    private Conf joinConf;
    private Model inst;
    private List<ModelDef> includedModels;
    private ModuleInst module;

    public ModelDefImpl(App app, String name, Conf conf) {
        setApp(app);
        setName(name);
        this.conf = conf;

        // определяем модуль
        String mp = "dbm/model/" + name;
        for (ModuleInst m : getApp().getModules()) {
            Conf a = m.getConf().findConf(mp);
            if (a != null) {
                this.module = m;
                break;
            }
        }

    }

    public Conf getConf() {
        return conf;
    }

    public Conf getJoinConf() {
        if (joinConf == null) {
            synchronized (this) {
                if (joinConf == null) {
                    joinConf = createJoinConf();
                }
            }
        }
        return joinConf;
    }

    protected Conf createJoinConf() {
        Conf tmp = Conf.create(getName());

        // накладываем все включенные модели
        for (ModelDef md : getIncludedModels()) {
            tmp.join(md.getConf());
        }

        // накладываем себя
        tmp.join(getConf());

        // обрабатываем

        // метим dbsource моделью
        Conf dbsConf = tmp.findConf("dbsource/default", true);
        dbsConf.setValue("modelName", this.getName());

        // накладываем конфигурацию для dbsource из cfg, если имеется
        // обрабатываем подстановки ${PROP}, где PROP - свойства dbsource, известные
        // с самого начала работы, т.е. переданные через тег, через который создается
        String cfgPath = dbsConf.getString("cfg");
        if (!UtString.empty(cfgPath)) {
            String cfgPathEx = UtString.substVar(cfgPath, v -> {
                return dbsConf.getString(v);
            });
            // сохраняем раскрытый
            dbsConf.setValue("cfg", cfgPathEx);
            //
            Conf cfgX = getApp().getConf().findConf(cfgPathEx);
            if (cfgX != null) {
                dbsConf.join(cfgX);
            }
        }

        // выясняем драйвер и его dbtype
        String dbsDbdriverName = dbsConf.getString("dbdriver");
        DbDriver dbdriverInst = getApp().bean(DbDriverService.class).getDbDriver(dbsDbdriverName);
        String dbtype = dbdriverInst.getDbType();

        // накладываем на модель нужный dbtype
        Conf dbtypeConf = tmp.findConf("dbtype/" + dbtype);
        if (dbtypeConf != null) {
            tmp.join(dbtypeConf);
        }

        // остальные dbtype убираем
        tmp.remove("dbtype");

        // метим модель нужным dbtype
        tmp.setValue("dbtype", dbtype);

        // меняем class для dbsource
        dbsConf.setValue("class", ModelDbSourceImpl.class.getName());

        // все

        return tmp;
    }

    public List<ModelDef> getIncludedModels() {
        if (includedModels == null) {
            synchronized (this) {
                if (includedModels == null) {
                    includedModels = Collections.unmodifiableList(createIncludedModels());
                }
            }
        }
        return includedModels;
    }

    protected List<ModelDef> createIncludedModels() {
        ModelService svc = getApp().bean(ModelService.class);
        ModelIncludeResolver r = new ModelIncludeResolver(svc);
        List<String> lst = r.resolveIncludeModel(getName());
        List<ModelDef> tmp = new ArrayList<>();
        for (String s : lst) {
            tmp.add(svc.getModels().get(s));
        }
        return tmp;
    }


    public Model createInst() {
        return getApp().create(conf, ModelImpl.class, inst -> {
            ((ModelImpl) inst).setModelDef(this);
        });
    }

    public Model getInst() {
        if (inst == null) {
            synchronized (this) {
                if (inst == null) {
                    inst = createInst();
                }
            }
        }
        return inst;
    }

    public boolean isInstance() {
        return false;
    }

    public ModelDef getInstanceOf() {
        return this;
    }

    public ModuleInst getModule() {
        return module;
    }

}
