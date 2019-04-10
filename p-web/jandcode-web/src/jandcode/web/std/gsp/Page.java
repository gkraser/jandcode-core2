package jandcode.web.std.gsp;

import jandcode.commons.variant.*;
import jandcode.web.gsp.*;

/**
 * Страница.
 * <p>
 * Тело тега - содержимое 'body' страницы.
 * Дополнительные элементы страницы (часть head например) определяется в
 * тегах {@code <jc:pagePart name="NAME">}. Их использование определяется конкретным
 * шаблоном.
 * <p>
 * Перед рендерингом шаблона template, сначала рендерится тело тега 'jc/page'.
 *
 * @args template имя шаблона, который нужно использовать для оформления страницы.
 * Может быть относительным путем
 */
public class Page extends BaseGsp {

    protected void onRender() throws Exception {
        GspArgsUtils ar = new GspArgsUtils(this);
        String template = ar.getPath("template", true);
        GspPageManager pm = inst(GspPageManager.class);
        pm.resetAddPart();

        // собираем аргументы с тега
        IVariantMap pa = pm.getArgs();
        for (String key : getArgs().keySet()) {
            if (!pa.containsKey(key)) {
                pa.put(key, getArgs().get(key));
            }
        }

        pm.addPart("body", this, false);
        include(template);
    }

}