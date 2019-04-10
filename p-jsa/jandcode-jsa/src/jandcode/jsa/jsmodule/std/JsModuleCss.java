package jandcode.jsa.jsmodule.std;

import jandcode.commons.*;
import jandcode.jsa.jsmodule.*;

import java.util.*;

/**
 * Модуль css
 */
public class JsModuleCss extends JsModuleText {

    protected void onInit(JsModuleBuilder b) throws Exception {
        super.onInit(b);

        //todo require для css нужно брать и только из оригинального текста

        String txt = b.getText();
        txt = txt.replace("\r", "");
        Map<String, Object> res = new LinkedHashMap<>();
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

}
