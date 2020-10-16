package jandcode.core.launcher.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.launcher.*;

import java.util.*;

/**
 * Поставщик cmd из описаний в conf
 */
public class CmdProviderConf extends BaseComp implements CmdProvider {

    public Collection<Cmd> loadCmds() throws Exception {
        List<Cmd> res = new ArrayList<>();
        Collection<Conf> lst = getApp().getConf().getConfs("launcher/cmd");
        for (Conf x : lst) {
            boolean enabled = x.getBoolean("enabled", true);
            if (!enabled) {
                continue;
            }
            String cls = x.getString("class");
            if (UtString.empty(cls)) {
                throw new XError("Не указан атрибут class для команды [{0}] ({1})", x.getName(), x.origin());
            }
            Cmd a = new CmdImpl(getApp(), x.getName(), cls);
            res.add(a);
        }
        return res;
    }
}
