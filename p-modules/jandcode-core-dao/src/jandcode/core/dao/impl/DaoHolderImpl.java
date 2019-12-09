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
    private String daoInvokerName;

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
        String daoInvoker = x.getString("dao-invoker", null);
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
                addItem(name, className, methodName, daoInvoker);
            } else {
                Class cls = UtClass.getClass(className);
                DaoClassDef cd = svc.getDaoClassDef(cls);
                for (DaoMethodDef md : cd.getMethods()) {
                    addItem(namePrefix + md.getName(), cls, md.getName(), daoInvoker);
                }
            }

        } else if (hasPak) {
            List<ReflectClazz> lst = UtReflect.getUtils().list(pak, recursive);
            String pakPfx = pak + ".";
            for (ReflectClazz clazz : lst) {
                Class cls = clazz.getCls();
                if (Dao.class.isAssignableFrom(cls)) {

                    //
                    String cn = cls.getSimpleName();
                    String cn1 = UtString.removeSuffix(cn, "_Dao");
                    if (cn1 != null) {
                        cn = cn1;
                    } else {
                        cn1 = UtString.removeSuffix(cn, "Dao");
                        if (cn1 != null) {
                            cn = cn1;
                        }
                    }
                    cn = UtString.uncapFirst(cn);

                    //
                    String pn = cls.getPackage().getName();
                    String pn1 = UtString.removePrefix(pn, pakPfx);
                    if (pn1 != null) {
                        pn = pn1 + "/";
                    } else {
                        pn = "";
                    }

                    cn = pn + cn;
                    //
                    DaoClassDef cd = svc.getDaoClassDef(cls);
                    //
                    for (DaoMethodDef md : cd.getMethods()) {
                        addItem(namePrefix + cn + "/" + md.getName(), cls, md.getName(), daoInvoker);
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
        String dmn = d.getDaoInvokerName();
        if (UtString.empty(dmn)) {
            dmn = getDaoInvokerName();
        }
        DaoInvoker dm = getApp().bean(DaoService.class).getDaoInvoker(dmn);
        return dm.invokeDao(d.getMethodDef(), args);
    }

    public NamedList<DaoHolderItem> getItems() {
        return items;
    }

    public String getDaoInvokerName() {
        if (UtString.empty(daoInvokerName)) {
            return "default";
        }
        return daoInvokerName;
    }

    public void setDaoInvokerName(String daoInvokerName) {
        this.daoInvokerName = daoInvokerName;
    }

    public DaoHolderItem addItem(String name, String className, String methodName, String daoInvokerName) {
        return addItem(name, UtClass.getClass(className), methodName, daoInvokerName);
    }

    public DaoHolderItem addItem(String name, Class cls, String methodName, String daoInvokerName) {
        DaoClassDef cd = getApp().bean(DaoService.class).getDaoClassDef(cls);
        DaoHolderItemImpl item = new DaoHolderItemImpl(name, cd.getMethods().get(methodName), daoInvokerName);
        this.items.add(item);
        return item;
    }

}
