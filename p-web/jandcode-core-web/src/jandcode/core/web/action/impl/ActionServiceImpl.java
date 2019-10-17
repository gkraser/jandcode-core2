package jandcode.core.web.action.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;

import java.util.*;

public class ActionServiceImpl extends BaseComp implements ActionService {

    private NamedList<ActionDef> actions;
    private List<IActionProvider> actionProviders = new ArrayList<>();
    private List<IActionFactory> actionFactorys = new ArrayList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        List<Conf> z;

        z = UtConf.sortByWeight(getApp().getConf().getConfs("web/action-provider"));
        for (Conf x : z) {
            IActionProvider q = (IActionProvider) getApp().create(x);
            actionProviders.add(q);
        }

        //
        z = UtConf.sortByWeight(getApp().getConf().getConfs("web/action-factory"));
        for (Conf x : z) {
            IActionFactory q = (IActionFactory) getApp().create(x);
            actionFactorys.add(q);
        }

    }

    public NamedList<ActionDef> getActions() {
        if (actions == null) {
            synchronized (this) {
                if (actions == null) {
                    actions = createActionDefs();
                }
            }
        }
        return actions;
    }

    /**
     * Создание списка определений actions
     */
    private NamedList<ActionDef> createActionDefs() {
        NamedList<ActionDef> tmp = new DefaultNamedList<>();
        //
        try {
            for (IActionProvider p : actionProviders) {
                List<ActionDef> z1 = p.loadActions();
                if (z1 != null) {
                    tmp.addAll(z1);
                }
            }
            // сортируем по фасетам-имени!
            tmp.sort(null);

        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
        return tmp;
    }

    public IAction createAction(Request request) {
        IAction a = null;
        for (int i = actionFactorys.size() - 1; i >= 0; i--) {
            IActionFactory p = actionFactorys.get(i);
            a = p.createAction(request);
            if (a != null) {
                break;
            }
        }
        return a;
    }

    public IAction createAction(String name) {
        ActionDef adef = getActions().find(name);
        if (adef == null) {
            throw new XError("Action {0} not found", name);
        }
        return adef.createInst();
    }

    public Iterable<IActionFactory> getActionFactorys() {
        List<IActionFactory> res = new ArrayList<>();
        for (int i = actionFactorys.size() - 1; i >= 0; i--) {
            IActionFactory p = actionFactorys.get(i);
            res.add(p);
        }
        return res;
    }

}
