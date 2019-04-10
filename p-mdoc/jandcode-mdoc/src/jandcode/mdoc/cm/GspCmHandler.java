package jandcode.mdoc.cm;

import jandcode.commons.variant.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.gsp.*;

/**
 * Специальный обработчик команды. Генерирует текст из gsp.
 * gsp получает атрибуты команды в аргументе attrs.
 */
public class GspCmHandler extends BaseCmHandler {

    private String gspTemplate = "not-assigned.gsp";

    /**
     * Шаблон. Имя исходного файла из документа.
     */
    public void setGspTemplate(String gspTemplate) {
        this.gspTemplate = gspTemplate;
    }

    public String handleCm(IVariantMap attrs, OutFile outFile) throws Exception {
        GspTemplateContext gctx = getOutBuilder().createGspTemplateContext(outFile);
        gctx.getArgs().put("attrs", attrs);
        return gctx.generate(this.gspTemplate);
    }

}
