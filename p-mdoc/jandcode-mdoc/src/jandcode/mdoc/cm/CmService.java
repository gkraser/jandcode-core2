package jandcode.mdoc.cm;

import jandcode.commons.*;
import jandcode.commons.attrparser.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.builder.*;

/**
 * Сервис для предоставления текста для команд в теге cm
 */
public class CmService extends BaseOutBuilderMember {

    public String resolveCm(String cmText, OutFile outFile) {
        if (cmText == null) {
            cmText = "";
        }
        cmText = cmText.trim();
        int a = cmText.indexOf(' ');
        String cm;
        IVariantMap attrs = new VariantMap();
        if (a == -1) {
            cm = cmText;
        } else {
            cm = cmText.substring(0, a);
            AttrParser prs = new AttrParser();
            prs.loadFrom(cmText.substring(a + 1));
            attrs.putAll(prs.getResult());
            attrs.put("$list", prs.getResultList());  // все атрибуты, включая дубли
        }
        if (UtString.empty(cm)) {
            return warning(outFile, cmText, "Команда отсутствует");
        }

        BeanDef bd = getOutBuilder().getBeanFactory().findBean("cm-" + cm);
        if (bd == null) {
            return warning(outFile, cmText, "Неизвестная команда");
        }

        try {
            CmHandler h = (CmHandler) bd.getInst();
            return h.handleCm(attrs, outFile);
        } catch (Exception e) {
            ErrorInfo ei = UtError.createErrorInfo(e);
            String cmTextNormalize = cmText.replaceAll("\\s+", " ").trim();
            String warnMsg = warning(outFile, cmTextNormalize, cm + ": " + ei.getText());
            if (getOutBuilder().getMode().isDebug()) {
                return warnMsg;
            } else {
                return warningHtml("error render");  // в prod убираем подробный warn
            }
        }
    }

    private String warning(OutFile outFile, String cmText, String msg) {
        String s = msg + " :: <cm> " + cmText + " </cm>";
        MDocLogger.getInst().warn(outFile, s);
        return warningHtml(s);
    }

    private String warningHtml(String s) {
        return "<span class='builder-warning'>" + UtString.xmlEscape(s) + "</span>";
    }

}
