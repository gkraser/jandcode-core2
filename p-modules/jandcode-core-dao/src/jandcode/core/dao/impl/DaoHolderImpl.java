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
    private String daoInvokerName;

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

    protected String classNameToDaoName(Class<?> cls) {
        String cn;
        cn = cls.getSimpleName();
        String cn1 = UtString.removeSuffix(cn, "_Dao");
        if (cn1 != null) {
            cn = cn1;
        } else {
            cn1 = UtString.removeSuffix(cn, "_dao");
            if (cn1 != null) {
                cn = cn1;
            } else {
                cn1 = UtString.removeSuffix(cn, "Dao");
                if (cn1 != null) {
                    cn = cn1;
                }
            }
        }
        cn = UtString.uncapFirst(cn);

        return cn;
    }

    protected boolean isClassUsed(Class cls) {
        for (var it : this.items) {
            if (cls == it.getMethodDef().getClassDef().getCls()) {
                return true;
            }
        }
        return false;
    }

    public void addItem(Conf x, String prefix) {
        String name = prefix;
        String namePrefix = name;
        boolean hasName = x.hasName();
        if (hasName) {
            name = name + UtConf.getNameAsPath(x);
            namePrefix = name + "/";
        }
        if (x.containsKey("prefix")) {
            namePrefix = x.getString("prefix");
            if (!UtString.empty(namePrefix)) {
                namePrefix = namePrefix + "/";
            }
        }

        Collection<Conf> childs = x.getConfs("item");
        String className = x.getString("class");
        String methodName = x.getString("method");
        String pak = x.getString("package");
        boolean recursive = x.getBoolean("recursive", true);
        boolean flat = x.getBoolean("flat");
        boolean skipUsed = x.getBoolean("skipUsed");
        String invoker = x.getString("invoker", null);
        String provider = x.getString("provider", null);

        boolean hasClass = !UtString.empty(className);
        boolean hasMethod = !UtString.empty(methodName);
        boolean hasPak = !UtString.empty(pak);

        //
        DaoService svc = getApp().bean(DaoService.class);

        if (!UtString.empty(provider)) {
            DaoHolderItemProvider prv = svc.getDaoHolderItemProviders().get(provider);
            Collection<DaoHolderItem> itms = prv.loadItems(x, namePrefix);
            for (DaoHolderItem it : itms) {
                this.items.add(it);
            }

        } else {

            if (hasClass) {
                if (hasMethod) {
                    if (methodName.indexOf('*') == -1) {
                        if (!hasName) {
                            throw new XError("При регистрации конкретного метода dao нужно указавать имя item");
                        }
                        addItem(name, className, methodName, invoker);
                    } else {
                        // имя метода с '*'
                        Class cls = UtClass.getClass(className);
                        DaoClassDef cd = svc.getDaoClassDef(cls);
                        for (DaoMethodDef md : cd.getMethods()) {
                            if (UtVDir.matchPath(methodName, md.getName())) {
                                addItem(namePrefix + md.getName(), cls, md.getName(), invoker);
                            }
                        }
                    }
                } else {
                    Class cls = UtClass.getClass(className);
                    DaoClassDef cd = svc.getDaoClassDef(cls);
                    String cn = classNameToDaoName(cls) + "/";
                    for (DaoMethodDef md : cd.getMethods()) {
                        addItem(namePrefix + cn + md.getName(), cls, md.getName(), invoker);
                    }
                }

            } else if (hasPak) {
                List<ReflectClazz> lst = UtReflect.getUtils().list(pak, recursive);
                String pakPfx = pak + ".";
                for (ReflectClazz clazz : lst) {
                    Class cls = clazz.getCls();
                    if (skipUsed && isClassUsed(cls)) {
                        continue;
                    }
                    if (Dao.class.isAssignableFrom(cls)) {

                        //
                        String cn = classNameToDaoName(cls);

                        //
                        String pn = cls.getPackage().getName();
                        String pn1 = UtString.removePrefix(pn, pakPfx);
                        if (pn1 != null && !flat) {
                            pn = pn1 + "/";
                        } else {
                            pn = "";
                        }

                        cn = pn + cn;
                        //
                        DaoClassDef cd = svc.getDaoClassDef(cls);
                        //
                        for (DaoMethodDef md : cd.getMethods()) {
                            addItem(namePrefix + cn + "/" + md.getName(), cls, md.getName(), invoker);
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
    }

    public DaoContext invokeDao(DaoContextIniter ctxIniter, String name, Object... args) throws Exception {
        DaoHolderItem d = items.get(name);
        String dmn = resolveDaoInvokerName(d);
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

    public String resolveDaoInvokerName(DaoHolderItem item) {
        String res = item.getDaoInvokerName();
        if (!UtString.empty(res)) {
            return res;
        }
        String nm = item.getName();
        for (Rule rule : this.rules) {
            if (UtVDir.matchPath(rule.mask, nm)) {
                return rule.daoInvoker;
            }
        }
        return getDaoInvokerName();
    }

    public String getDaoInvokerName() {
        return UtString.empty(this.daoInvokerName) ? "default" : this.daoInvokerName;
    }

    // что бы срабатывало присвоение из атрибутов conf: invoker='default'
    public void setInvokerName(String v) {
        this.daoInvokerName = v;
    }

    public void addRule(String mask, String daoInvoker) {
        this.rules.add(0, new Rule(mask, daoInvoker));
    }

    public void addItem(String name, String className, String methodName, String daoInvokerName) {
        addItem(name, UtClass.getClass(className), methodName, daoInvokerName);
    }

    public void addItem(String name, Class cls, String methodName, String daoInvokerName) {
        DaoClassDef cd = getApp().bean(DaoService.class).getDaoClassDef(cls);
        DaoHolderItemImpl item = new DaoHolderItemImpl(name, cd.getMethods().get(methodName), daoInvokerName);
        this.items.add(item);
    }

}
