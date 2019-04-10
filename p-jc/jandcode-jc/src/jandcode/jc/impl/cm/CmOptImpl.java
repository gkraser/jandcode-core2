package jandcode.jc.impl.cm;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;

public class CmOptImpl implements CmOpt {

    private String name;
    private String help;
    private Object defaultValue;

    public CmOptImpl(String name, Object defaultValue, String help) {
        if (JcConsts.isOptSys(name)) {
            throw new XError("Опция [{0}] зарезервирована и не может быть использована в команде", name);
        }
        this.name = name;
        if (defaultValue == null) {
            this.defaultValue = false;
        } else {
            this.defaultValue = defaultValue;
        }
        this.help = help;
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return UtString.empty(help) ? UtLang.t(JcConsts.NO_HELP) : help;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
