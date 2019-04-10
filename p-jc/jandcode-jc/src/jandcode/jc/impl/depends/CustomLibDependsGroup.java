package jandcode.jc.impl.depends;

import jandcode.commons.error.*;
import jandcode.jc.*;

import java.util.*;

public abstract class CustomLibDependsGroup implements LibDependsGroup {

    protected Ctx ctx;
    protected String name;
    protected Object own;

    public CustomLibDependsGroup(Ctx ctx) {
        assert ctx != null;
        this.ctx = ctx;
    }

    public String getName() {
        return name;
    }

    protected String getOwn() {
        return InternalLibDependsUtils.ownToText(own);
    }

    protected Ctx getCtx() {
        if (ctx == null) {
            throw new XError("ctx not assigned");
        }
        return ctx;
    }

    public ListLib getLibs() {
        ListLib res = new ListLib();
        for (String nm : getNames()) {
            Lib lib = getCtx().findLib(nm);
            if (lib == null) {
                throw new XError("Не найдена библиотека [{0}] из группы [{1}] в {2}", nm, getName(), getOwn());
            }
            res.add(lib);
        }
        return res;
    }

    public ListLib getLibsAll() {
        return getCtx().getLibs(getLibs());
    }

    public boolean isEmpty() {
        return getNames().isEmpty();
    }

    public List<String> getModules() {
        Set<String> res = new LinkedHashSet<>();
        for (String nm : getNames()) {
            Lib lib = getCtx().findLibForModule(nm);
            if (lib != null) {
                // это модуль
                res.add(nm);
            } else {
                lib = getCtx().findLib(nm);
                if (lib != null) {
                    // это библиотека, добавляем все модули из нее
                    res.addAll(lib.getModules());
                } else {
                    throw new XError("Не найдена библиотека или модуль [{0}] из группы [{1}] в {2}", nm, getName(), getOwn());
                }
            }
        }
        return new ArrayList<>(res);
    }

}
