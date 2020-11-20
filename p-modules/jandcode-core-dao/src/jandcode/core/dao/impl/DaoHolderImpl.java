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
    private List<Rule> rules;

    static class Rule implements DaoHolderRule {
        String mask;
        String daoInvoker;

        public Rule(String mask, String daoInvoker) {
            this.mask = mask;
            this.daoInvoker = daoInvoker;
        }

        public String getMask() {
            return mask;
        }

        public String getInvoker() {
            return daoInvoker;
        }
    }

    public DaoHolderImpl() {
        this.items = new DefaultNamedList<>();
        this.items.setNotFoundMessage("Не найден dao-метод {0} в dao-хранилище {1}", this);
        this.rules = new ArrayList<>();
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        for (Conf ruleConf : cfg.getConf().getConfs("rule")) {
            String daoInvoker = ruleConf.getString("invoker");
            String mask = ruleConf.getString("mask");
            if (UtString.empty(daoInvoker)) {
                throw new XError("Атрибут invoker пустой: {0}", ruleConf.origin());
            }
            if (UtString.empty(mask)) {
                throw new XError("Атрибут mask пустой: {0}", ruleConf.origin());
            }
            addRule(mask, daoInvoker);
        }

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
                addItem(name, className, methodName);
            } else {
                Class cls = UtClass.getClass(className);
                DaoClassDef cd = svc.getDaoClassDef(cls);
                for (DaoMethodDef md : cd.getMethods()) {
                    addItem(namePrefix + md.getName(), cls, md.getName());
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
                        addItem(namePrefix + cn + "/" + md.getName(), cls, md.getName());
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

    public DaoContext invokeDao(DaoContextIniter ctxIniter, String name, Object... args) throws Exception {
        DaoHolderItem d = items.get(name);
        String dmn = d.getDaoInvokerName();
        DaoInvoker dm = getApp().bean(DaoService.class).getDaoInvoker(dmn);
        return dm.invokeDao(ctxIniter, d.getMethodDef(), args);
    }

    public Object invokeDao(String name, Object... args) throws Exception {
        DaoContext ctx = invokeDao(null, name, args);
        return ctx.getResult();
    }

    public NamedList<DaoHolderItem> getItems() {
        return items;
    }

    public Collection<DaoHolderRule> getRules() {
        return (Collection) rules;
    }

    public String resolveDaoInvokerName(String itemName) {
        for (Rule rule : this.rules) {
            if (UtVDir.matchPath(rule.mask, itemName)) {
                return rule.daoInvoker;
            }
        }
        return "default";
    }

    public void addRule(String mask, String daoInvoker) {
        this.rules.add(0, new Rule(mask, daoInvoker));
    }

    public void addItem(String name, String className, String methodName) {
        addItem(name, UtClass.getClass(className), methodName);
    }

    public void addItem(String name, Class cls, String methodName) {
        DaoClassDef cd = getApp().bean(DaoService.class).getDaoClassDef(cls);
        DaoHolderItemImpl item = new DaoHolderItemImpl(this, name, cd.getMethods().get(methodName));
        this.items.add(item);
    }

}
