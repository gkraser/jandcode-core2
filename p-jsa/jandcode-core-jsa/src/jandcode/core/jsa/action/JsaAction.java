package jandcode.core.jsa.action;

import jandcode.commons.*;
import jandcode.commons.error.impl.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.jsa.jsmodule.std.*;
import jandcode.core.web.action.*;
import org.slf4j.*;

import java.util.*;

/**
 * action для jsa
 */
public class JsaAction extends BaseAction {

    protected static Logger log = LoggerFactory.getLogger(JsaAction.class);

    private JsModuleService svc;

    protected void onExec() throws Exception {
        this.svc = getApp().bean(JsModuleService.class);
        //
        super.onExec();
    }

    protected void render(IModuleText txt) {
        String etag = txt.getHash();
        getReq().checkETag(etag);
        String mimeJs = getReq().getWebService().findFileType("js").getMime();
        getReq().setContentType(mimeJs);
        getReq().render(txt.getText());
    }

    protected void renderError(Exception e) {
        String msg = new ErrorFormatterDefault(true, true, false).
                getMessage(UtError.createErrorInfo(e));
        log.error("ERROR =>\n\n" + msg + "\n");
        render(new ModuleTextError(getApp(), e));
    }

    /**
     * Параметр p как строка
     */
    protected String getPStr(String paramName) {
        return getReq().getString(paramName);
    }

    /**
     * Параметр p как список строк
     */
    protected List<String> getPList(String paramName) {
        return UtCnv.toList(getPStr(paramName));
    }

    /**
     * Возвращает текст jsmodule (script)
     */
    public void s() throws Exception {
        JsModule m = null;
        try {
            String p = getPStr("p");
            m = svc.getModule(p);
            m.getText(); // проверка, что все нормально
        } catch (Exception e) {
            renderError(e);
            return;
        }
        render(m);
    }

    /**
     * Список зависимостей (requires)
     */
    public void r() throws Exception {
        String paths = getPStr("p");
        ModuleTextRequires a = new ModuleTextRequires(getApp(), paths);
        render(a);
    }

    /**
     * Список текстов (texts)
     */
    public void t() throws Exception {
        String p = getPStr("p");
        List<String> ids = JsModuleUtils.strToIdList(p);
        ModuleTextOnlyModules a = new ModuleTextOnlyModules(getApp(), ids);
        render(a);
    }

    /**
     * Определения модулей (modules)
     */
    public void m() throws Exception {
        ModuleTextModules a = null;
        try {
            String p = getPStr("p");
            List<String> ids = JsModuleUtils.strToIdList(p);
            String e = getPStr("e");
            List<String> idsExc = JsModuleUtils.strToIdList(e);
            //
            a = new ModuleTextModules(getApp(), ids, idsExc);
            a.getText();  // проверка, что все нормально
        } catch (Exception e1) {
            renderError(e1);
            return;
        }
        render(a);
    }

}
