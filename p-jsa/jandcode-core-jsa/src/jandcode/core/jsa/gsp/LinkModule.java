package jandcode.core.jsa.gsp;

import jandcode.commons.*;
import jandcode.core.web.gsp.*;

import java.util.*;

/**
 * Вывести тег для подключения модулей
 *
 * @arg module модули через ',' или список модулей. Для каждого модуля будет выведен отдельный тег
 */
public class LinkModule extends BaseGsp {

    class LaterOut implements IGspLaterHandler {

        private int key;

        public LaterOut(int key) {
            this.key = key;
        }

        public void outLater(Gsp g) {
            inst(LinkModuleManager.class).outLink(g, key);
        }
    }

    protected void onRender() throws Exception {
        GspArgsUtils ar = new GspArgsUtils(this);

        Object module = ar.getValue("module", true);

        // страницы, на которых используется linkModule, не должны кешироватся
        getRequest().disableCache();

        // каждый модуль выводим отдельным тегом
        List<String> lstPath = UtCnv.toList(module);
        for (String p1 : lstPath) {
            // регистрируем
            int key = inst(LinkModuleManager.class).addModules(p1);
            // выводить будем позже
            out(new LaterOut(key));
        }
    }
}
