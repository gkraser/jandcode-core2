package jandcode.jc.impl.cm;

import groovy.lang.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.jc.*;

import java.util.*;

public class CmHolderImpl implements CmHolder {

    private Project project;
    private NamedList<Cm> items = new DefaultNamedList<Cm>("Команда [{0}] не найдена");

    public CmHolderImpl(Project project) {
        this.project = project;
    }

    public Collection<Cm> getItems() {
        return items;
    }

    public Cm add(String cmName, Object... args) {
        Cm cm = createCmInst(cmName, args);
        items.add(cm);
        return cm;
    }

    public void remove(String cmName) {
        cmName = toCmName(cmName);
        items.remove(cmName);
    }

    public Cm find(String cmName) {
        cmName = toCmName(cmName);
        return items.find(cmName);
    }

    public Cm get(String cmName) {
        cmName = toCmName(cmName);
        return items.get(cmName);
    }

    public void exec(String cmName) {
        get(cmName).exec();
    }

    public void exec(String cmName, Map args) {
        get(cmName).exec(args);
    }

    public void exec(Map args, String cmName) {
        get(cmName).exec(args);
    }

    //////

    public CmOpt opt(String name, Object defaultValue, String help) {
        return new CmOptImpl(name, defaultValue, help);
    }

    public CmOpt opt(String name, String help) {
        return opt(name, false, help);
    }

    public CmOpt opt(String name, Closure cls) {
        return opt(name, cls, null);
    }

    //////

    protected String toCmName(String cmName) {
        return cmName.replace('_', '-');
    }

    protected Cm createCmInst(String name, Object... args) {
        CmImpl res = new CmImpl(toCmName(name));
        boolean helpAssigned = false;
        boolean runAssigned = false;

        // раскрываем списки в аргументах
        List argsList = new ArrayList<>();
        toFlatListArgs(argsList, args);

        for (Object arg : argsList) {
            if (arg instanceof CharSequence) {
                if (helpAssigned) {
                    throw new XError("Указано 2 аргумента для help");
                }
                helpAssigned = true;
                res.setHelp(arg.toString());
            } else if (arg instanceof Closure) {
                if (runAssigned) {
                    throw new XError("Указано 2 аргумента для doRun");
                }
                runAssigned = true;
                res.setDoRun((Closure) arg);
            } else if (arg instanceof CmOpt) {
                res.getOpts().add((CmOpt) arg);
            } else {
                throw new XError("Неизвестный тип аргумента");
            }
        }
        return res;
    }

    /**
     * Разворачиваем аргументы в список
     *
     * @param lst  куда
     * @param args что
     */
    protected void toFlatListArgs(List lst, Object... args) {
        if (args == null) {
            return;
        }
        for (Object arg : args) {
            if (arg instanceof List) {
                for (Object itm : (List) arg) {
                    toFlatListArgs(lst, itm);
                }
            } else {
                lst.add(arg);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<CmOpt> getOpts(Cm cm, Map args) {
        if (args == null) {
            args = new HashMap();
        }
        NamedList<CmOpt> res = new DefaultNamedList<CmOpt>();
        for (CmOpt opt : cm.getOpts()) {
            if (opt.getDefaultValue() instanceof Closure) {
                List dynopts = (List) ((Closure) opt.getDefaultValue()).call(args);
                if (dynopts != null) {
                    res.addAll(dynopts);
                }
            } else {
                res.add(opt);
            }
        }
        return res;
    }

}
