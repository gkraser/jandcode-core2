package jandcode.core.jsa.jsmodule.std;

import jandcode.commons.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.jsa.utils.*;
import org.slf4j.*;

import java.util.*;

/**
 * Модуль css
 */
public class JsModuleCss extends JsModuleText {

    protected static Logger log = LoggerFactory.getLogger(JsModuleCss.class);

    protected void onInit(JsModuleBuilder b) throws Exception {
        super.onInit(b);

        String txt = b.getText();
        txt = txt.replace("\r", "");
        Map<String, Object> res = new LinkedHashMap<>();

        if (txt.indexOf("url(") != -1) {
            if (log.isInfoEnabled()) {
                log.info("rebaseUrl for: " + getName());
            }
            CssUrlRebase rb = new CssUrlRebase();
            String prefix = "~~BASEURL~~";
            String newTxt = rb.rebase(txt, UtFile.path(getName()), "/", prefix);

            if (rb.isReplaced()) {
                res.put("rebaseUrl", prefix);
                txt = newTxt;
            } else {
                if (log.isInfoEnabled()) {
                    log.info("not need rebaseUrl for: " + getName());
                }
            }
        }

        res.put("css", true);
        res.put("text", txt);

        StringBuilder sb = new StringBuilder();
        sb.append("module.exports = ").append(UtJson.toJson(res)).append(";\n");
        sb.append("module.exports.filename = __filename;\n");

        b.setText(sb.toString());
    }

    protected String prepareTextAfterGsp(String s) {
        return s.replaceAll("<style.*?>", "").replaceAll("</style>", "");
    }

}
