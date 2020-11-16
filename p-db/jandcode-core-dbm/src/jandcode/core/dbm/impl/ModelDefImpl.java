package jandcode.core.dbm.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.db.impl.*;

import java.util.*;

/**
 * Описание модели, полученное из conf
 */
public class ModelDefImpl extends BaseComp implements ModelDef {

    private Conf conf;
    private Conf joinConf;
    private Model inst;
    private List<ModelDef> includedModels;
    private DbMode dbMode;
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
        Conf tmp = UtConf.create(getName());

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

    public boolean isDefinedHere(String dbtype, String idn) {

        // сначала в dbtype
        Conf x = getConf().findConf("dbtype/" + dbtype + "/" + idn);
        if (x != null) {
            return true;
        }

        // потом в самой модели
        return getConf().findConf(idn) != null;

    }

    public boolean isDefinedForDbStruct(String dbtype, String idn) {
        if (getDbMode() == DbMode.none) {
            return false; // не может иметь структуру
        }
        if (getDbMode() == DbMode.solid) {
            return true;  // все равно имеет этот объект
        }

        // нужно что бы объект был определен в модели до первой самостоятельной структуры
        List<ModelDef> lst = getIncludedModels();
        for (int i = 0; i < lst.size(); i++) {
            ModelDef md1 = lst.get(i);
            if (md1.isDefinedHere(dbtype, idn)) {
                // определен в этой модели, но моя ли она?
                for (int j = lst.size() - 1; j >= i; j--) {
                    ModelDef md2 = lst.get(j);
                    if (md2.getDbMode() != DbMode.none) {
                        return false; // все, модели кончились
                    }
                    if (md1 == md2) {
                        return true;
                    }
                }
                return false;
            }
        }
        // ни у кого нету. Может у меня есть?
        return isDefinedHere(dbtype, idn);
    }

    public DbMode getDbMode() {
        if (dbMode == null) {
            dbMode = DbMode.fromString(getConf().getString("dbmode"));
        }
        return dbMode;
    }

    public ModuleInst getModule() {
        return module;
    }

}
