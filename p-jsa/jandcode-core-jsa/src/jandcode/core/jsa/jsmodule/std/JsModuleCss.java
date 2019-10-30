package jandcode.core.jsa.jsmodule.std;

import jandcode.commons.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.jsa.utils.*;
import jandcode.core.web.*;

import java.util.*;

/**
 * Модуль css
 */
public class JsModuleCss extends JsModuleText {

    private boolean urlRebase;

    protected void onInit(JsModuleBuilder b) throws Exception {
        super.onInit(b);

        //todo require для css нужно брать и только из оригинального текста

        String txt = b.getText();
        txt = txt.replace("\r", "");
        Map<String, Object> res = new LinkedHashMap<>();

        if (isUrlRebase()) {
            CssUrlRebase rb = new CssUrlRebase();
            WebService webSvc = getApp().bean(WebService.class);
            String prefix = webSvc.getRequest().ref("/");  //todo получить ref вне request
            txt = rb.rebase(txt, UtFile.path(getName()), "/", prefix);
        }

        res.put("text", txt);

        List<String> reqs = new ArrayList<>();
        res.put("requires", reqs);
        res.put("css", true);

        StringBuilder sb = new StringBuilder();
        sb.append("module.exports = ").append(UtJson.toJson(res)).append(";\n");
        sb.append("module.exports.filename = __filename;\n");

        b.setText(sb.toString());
    }

    protected String prepareTextAfterGsp(String s) {
        return s.replaceAll("<style.*?>", "").replaceAll("</style>", "");
    }

    public boolean isUrlRebase() {
        return urlRebase;
    }

    public void setUrlRebase(boolean urlRebase) {
        this.urlRebase = urlRebase;
    }
}
