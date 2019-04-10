package jandcode.jsa.gsp;

import jandcode.web.gsp.*;

/**
 * Вывести тег для подключения модулей
 *
 * @arg path модули через ','
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
        String path = ar.getPath("path");

        // страницы, на которых используется linkModule, не должны кешироватся
        getRequest().disableCache();

        // регистрируем
        int key = inst(LinkModuleManager.class).addModules(path);

        // выводить будем позже
        out(new LaterOut(key));
    }
}
