package jandcode.mdoc.cm;

import jandcode.commons.variant.*;
import jandcode.mdoc.builder.*;

/**
 * Значение из свойств документа документа
 * name - имя свойства
 */
public class PropCmHandler extends BaseCmHandler {

    public String handleCm(IVariantMap attrs, OutFile outFile) throws Exception {
        String prop = attrs.getString("name");
        return getOutBuilder().getDoc().getCfg().getProps().getString(prop);
    }

}
