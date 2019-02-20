package jandcode.jc.impl.depends;

import jandcode.commons.*;
import jandcode.jc.*;

import java.util.*;

public class LibDependsGroupImpl extends CustomLibDependsGroup {

    private Set<String> libNames = new LinkedHashSet<>();

    public LibDependsGroupImpl(String name, Object own, Ctx ctx) {
        super(ctx);
        this.name = name;
        this.own = own;
    }

    public List<String> getNames() {
        return new ArrayList<>(libNames);
    }

    public void add(Object... libNames) {
        if (libNames == null) {
            return;
        }
        for (Object it : libNames) {
            List<String> tmp = UtCnv.toNameList(it);
            this.libNames.addAll(tmp);
        }
    }

    public void remove(Object libNames) {
        List<String> tmp = UtCnv.toNameList(libNames);
        this.libNames.removeAll(tmp);
    }

    public boolean contains(Object libNames) {
        List<String> tmp = UtCnv.toNameList(libNames);
        for (String n : tmp) {
            if (this.libNames.contains(n)) {
                return true;
            }
        }
        return false;
    }

}
