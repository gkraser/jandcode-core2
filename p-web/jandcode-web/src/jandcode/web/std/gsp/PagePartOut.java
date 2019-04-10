package jandcode.web.std.gsp;

import jandcode.web.gsp.*;

/**
 * Вывести поименнованную часть страницы
 *
 * @arg name имя части
 * @arg body текст, который выводится в случае, если части нет
 */
public class PagePartOut extends BaseGsp {

    protected void onRender() throws Exception {
        GspArgsUtils ar = new GspArgsUtils(this);
        String nm = ar.getString("name", true);
        GspPageManager pm = inst(GspPageManager.class);
        if (!pm.hasPart(nm)) {
            outBody();
        } else {
            pm.outPart(nm);
        }
    }

}
