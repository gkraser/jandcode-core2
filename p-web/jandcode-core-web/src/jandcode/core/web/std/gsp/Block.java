package jandcode.core.web.std.gsp;

import jandcode.core.web.gsp.*;

/**
 * Этот тег при первой попытке вывода запоминается в аргументах владельца.
 * Следующие попытки вывода приведут к выводу его body.
 *
 * @arg name имя аргумента владельца, в который записывается ссылка на блок
 */
public class Block extends BaseGsp {

    private boolean saved;

    protected void onRender() throws Exception {
        if (!saved) {
            saved = true;
            setRemoveFromPool(true);
            GspArgsUtils ar = new GspArgsUtils(this);
            String nm = ar.getString("name", true);
            getOwner().getArgs().put(nm, this);
        } else {
            outBody();
        }
    }

}
