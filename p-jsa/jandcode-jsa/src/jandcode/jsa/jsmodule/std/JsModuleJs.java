package jandcode.jsa.jsmodule.std;

import jandcode.commons.*;
import jandcode.jsa.jsmodule.*;
import jandcode.jsa.jsmodule.impl.*;
import jandcode.web.virtfile.*;

import java.util.*;

/**
 * Обычный модуль js
 */
public class JsModuleJs extends JsModuleText {

    private boolean extractRequire = true;

    protected void onInit(JsModuleBuilder b) throws Exception {
        super.onInit(b);

        //
        VirtFile reqFile = b.findCompiled("req");
        if (reqFile != null) {
            Object jsn = UtJson.fromJson(reqFile.loadText());
            if (jsn instanceof List) {
                for (Object r : (List) jsn) {
                    b.addRequire(UtString.toString(r));
                }
            }
        }

        if (isExtractRequire() && reqFile == null) {
            List<String> reqs = new RequireExtractor().extractRequire(b.getText());
            for (String p : reqs) {
                b.addRequire(p);
            }
        }

    }

    protected String prepareTextAfterGsp(String s) {
        return s.replaceAll("<script.*?>", "").replaceAll("</script>", "");
    }

    //////

    public boolean isExtractRequire() {
        return extractRequire;
    }

    /**
     * false - не извлекать require, т.к. это библиотека самодостаточная
     */
    public void setExtractRequire(boolean extractRequire) {
        this.extractRequire = extractRequire;
    }
}
