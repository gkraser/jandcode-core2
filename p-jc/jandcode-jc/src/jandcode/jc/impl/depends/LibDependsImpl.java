package jandcode.jc.impl.depends;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.jc.*;

public class LibDependsImpl implements LibDepends {

    private Object own;
    private Ctx ctx;
    private LibDependsGroup allGroup;
    private NamedList<LibDependsGroup> groups = new DefaultNamedList<>();

    public LibDependsImpl(Ctx ctx, Object own) {
        this.ctx = ctx;
        this.own = own;
        addGroup(JcConsts.DEPENDS_PROD);
        addGroup(JcConsts.DEPENDS_DEV);
    }

    public Ctx getCtx() {
        return ctx;
    }

    public LibDependsGroup getGroup(String name) {
        LibDependsGroup g = groups.find(name);
        if (g == null) {
            throw new XError("Не найдена группа зависимостей {0} в {1}", name, InternalLibDependsUtils.ownToText(own));
        }
        return g;
    }

    public LibDependsGroup addGroup(String name) {
        LibDependsGroup g = groups.find(name);
        if (g == null) {
            g = new LibDependsGroupImpl(name, own, ctx);
            groups.add(g);
        }
        return g;
    }

    public NamedList<LibDependsGroup> getGroups() {
        return groups;
    }

    public void validate() {
        for (LibDependsGroup g : getGroups()) {
            g.getLibs();
        }
    }

    public LibDependsGroup getProd() {
        return getGroups().get(JcConsts.DEPENDS_PROD);
    }

    public LibDependsGroup getDev() {
        return getGroups().get(JcConsts.DEPENDS_DEV);
    }

    public LibDependsGroup getAll() {
        if (allGroup == null) {
            validate();
            allGroup = new LibDependsGroupAll(JcConsts.DEPENDS_ALL, own, getGroups(), ctx);
        }
        return allGroup;
    }

}
