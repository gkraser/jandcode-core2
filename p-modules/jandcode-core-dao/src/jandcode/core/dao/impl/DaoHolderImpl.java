package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.reflect.*;
import jandcode.core.*;
import jandcode.core.dao.*;

import java.util.*;

public class DaoHolderImpl extends BaseComp implements DaoHolder {

    private NamedList<DaoHolderItem> items;
    private String daoManagerName;

    public DaoHolderImpl() {
        this.items = new DefaultNamedList<>();
        this.items.setNotFoundMessage("Не найден dao-метод {0} в dao-хранилище {1}", this);
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        for (Conf itemConf : cfg.getConf().getConfs("item")) {
            try {
                addItem(itemConf, "");
            } catch (Exception e) {
                throw new XErrorMark(e, itemConf.origin().toString());
            }
        }
    }

    public void addItem(Conf x, String prefix) {
        String name = prefix;
        String namePrefix = name;
        boolean hasName = x.hasName();
        if (hasName) {
            name = name + UtConf.getNameAsPath(x);
            namePrefix = name + "/";
        }

        Collection<Conf> childs = x.getConfs("item");
        String className = x.getString("class");
        String methodName = x.getString("method");
        String daoManager = x.getString("dao-manager", null);
        String pak = x.getString("package");
        boolean recursive = x.getBoolean("recursive", true);

        boolean hasClass = !UtString.empty(className);
        boolean hasMethod = !UtString.empty(methodName);
        boolean hasPak = !UtString.empty(pak);

        //
        DaoService svc = getApp().bean(DaoService.class);

        if (hasClass) {
            if (hasMethod) {
                if (!hasName) {
                    throw new XError("Имя не указано");
                }
                addItem(name, className, methodName, daoManager);
            } else {
                Class cls = UtClass.getClass(className);
                DaoClassDef cd = svc.getDaoClassDef(cls);
                for (DaoMethodDef md : cd.getMethods()) {
                    addItem(namePrefix + md.getName(), cls, md.getName(), daoManager);
                }
            }

        } else if (hasPak) {
            List<ReflectClazz> lst = UtReflect.getUtils().list(pak, recursive);
            String pakPfx = pak + ".";
            for (ReflectClazz clazz : lst) {
                Class cls = clazz.getCls();
                if (BaseDao.class.isAssignableFrom(cls)) {
                    String nm = UtString.removePrefix(cls.getName(), pakPfx);
                    if (nm == null) {
                        // так не должно быть...
                        continue;
                    }
                    nm = nm.replace('.', '/');
                    String nm1 = UtString.removeSuffix(nm, "_Dao");
                    if (nm1 != null) {
                        nm = nm1;
                    } else {
                        nm1 = UtString.removeSuffix(nm, "Dao");
                        if (nm1 != null) {
                            nm = nm1;
                        }
                    }
                    nm = UtString.uncapFirst(nm);
                    //
                    DaoClassDef cd = svc.getDaoClassDef(cls);
                    //
                    for (DaoMethodDef md : cd.getMethods()) {
                        addItem(namePrefix + nm + "/" + md.getName(), cls, md.getName(), daoManager);
                    }
                }
            }
        }

        // дочерние
        if (childs.size() > 0) {
            for (Conf child : childs) {
                addItem(child, namePrefix);
            }
        }
    }

    public Object invokeDao(String name, Object... args) throws Exception {
        DaoHolderItem d = items.get(name);
        String dmn = d.getDaoManagerName();
        if (UtString.empty(dmn)) {
            dmn = getDaoManagerName();
        }
        DaoManager dm = getApp().bean(DaoService.class).getDaoManager(dmn);
        return dm.invokeDao(d.getMethodDef(), args);
    }

    public NamedList<DaoHolderItem> getItems() {
        return items;
    }

    public String getDaoManagerName() {
        if (UtString.empty(daoManagerName)) {
            return "default";
        }
        return daoManagerName;
    }

    public void setDaoManagerName(String daoManagerName) {
        this.daoManagerName = daoManagerName;
    }

    public DaoHolderItem addItem(String name, String className, String methodName, String daoManagerName) {
        return addItem(name, UtClass.getClass(className), methodName, daoManagerName);
    }

    public DaoHolderItem addItem(String name, Class cls, String methodName, String daoManagerName) {
        DaoClassDef cd = getApp().bean(DaoService.class).getDaoClassDef(cls);
        DaoHolderItemImpl item = new DaoHolderItemImpl(name, cd.getMethods().get(methodName), daoManagerName);
        this.items.add(item);
        return item;
    }

}
