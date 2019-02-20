package jandcode.jc.impl.depends;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;

import java.util.*;

public class LibDependsGroupAll extends CustomLibDependsGroup {

    private List<LibDependsGroup> groups;

    public LibDependsGroupAll(String name, Object own, List<LibDependsGroup> groups, Ctx ctx) {
        super(ctx);
        this.name = name;
        this.own = own;
        this.groups = groups;
    }

    public List<String> getNames() {
        List<String> res = new ArrayList<>();
        Set<String> tmp = new LinkedHashSet<>();
        for (LibDependsGroup g : groups) {
            tmp.addAll(g.getNames());
        }
        res.addAll(tmp);
        return res;
    }

    public void add(Object... libNames) {
        checkModify();
    }

    public void remove(Object libNames) {
        checkModify();
    }

    private void checkModify() {
        throw new XError("Группа {0} в {1} виртуальная и не может быть изменена", getName(), getOwn());
    }

    public boolean contains(Object libNames) {
        List<String> tmp = UtCnv.toNameList(libNames);
        List<String> nms = getNames();
        for (String n : tmp) {
            if (nms.contains(n)) {
                return true;
            }
        }
        return false;
    }

}
