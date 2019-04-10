package jandcode.jc.impl.cm;

import groovy.lang.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.jc.*;
import jandcode.jc.impl.just.*;

import java.util.*;

public class CmImpl implements Cm {

    private String name;
    private String help;
    private NamedList<CmOpt> options = new DefaultNamedList<CmOpt>();
    private Closure doRunClosure;

    public CmImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return UtString.empty(help) ? UtLang.t(JcConsts.NO_HELP) : help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public NamedList<CmOpt> getOpts() {
        return options;
    }

    public void setDoRun(Closure closure) {
        doRunClosure = closure;
    }

    public void exec(Map args) {
        if (doRunClosure == null) {
            throw new XError("Для команды не определена doRunClosure");
        }
        //
        CmArgs args1 = createArgs(args);
        if (doRunClosure.getParameterTypes().length == 0) {
            doRunClosure.call();
        } else {
            doRunClosure.call(args1);
        }
    }

    public void exec() {
        exec(null);
    }

    /**
     * Создать аргументы для команды по map
     */
    protected CmArgs createArgs(Map map) {
        CmArgs res = new CmArgsImpl();

        // для начала просто перекачиваем все что передали
        if (map != null) {
            for (Object key : map.keySet()) {
                String nm = key.toString();
                res.put(nm, map.get(key));
            }
        }

        // конвертируем опции в правильные типы
        for (CmOpt opt : options) {
            if (res.containsKey(opt.getName())) {
                VariantDataType dt = VariantDataType.fromObject(opt.getDefaultValue());
                Object v = res.get(opt.getName());
                if (dt == VariantDataType.BOOLEAN) {
                    // для boolean: CliMap ставит пустую строку в качестве значения для опции
                    if (v instanceof String && ((String) v).length() == 0) {
                        // но фактически опция то есть, ставим true
                        res.put(opt.getName(), true);
                    } else {
                        // что передали, то и ставим
                        res.put(opt.getName(), VariantDataType.toDataType(v, dt));
                    }
                } else {
                    // для остальных просто конвертируем в нужный тип
                    res.put(opt.getName(), VariantDataType.toDataType(v, dt));
                }
            }
        }
        //
        return res;
    }

}
