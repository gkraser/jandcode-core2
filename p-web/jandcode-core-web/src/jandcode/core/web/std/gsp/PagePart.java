package jandcode.core.web.std.gsp;

import jandcode.core.web.gsp.*;

/**
 * Часть страницы. Такой тег добавляется в {@link GspPageManager}.
 *
 * @arg name имя части
 * @arg deffer Если false, то тело тега рендерится сразу, в момент объявления,
 * но не выводится, а запоминается. Если true - то тело не будет рендерится до явного
 * включения части в тело страницы. По умолчанию - true.
 */
public class PagePart extends BaseGsp {

    protected void onRender() throws Exception {
        setRemoveFromPool(true);
        //
        GspArgsUtils ar = new GspArgsUtils(this);
        String nm = ar.getString("name", true);
        boolean deffer = ar.getBoolean("deffer", true);
        GspPageManager pm = inst(GspPageManager.class);
        pm.addPart(nm, this, deffer);
        //
    }

}
