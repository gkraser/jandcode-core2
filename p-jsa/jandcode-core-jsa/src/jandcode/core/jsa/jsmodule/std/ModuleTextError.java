package jandcode.core.jsa.jsmodule.std;


import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;

/**
 * Генерит вывод на консоль ошибки
 */
public class ModuleTextError extends ModuleTextCustom {

    private Exception exc;

    public ModuleTextError(App app, Exception exc) {
        setApp(app);
        this.exc = exc;
    }

    protected String makeText() {
        StringBuilder sb = new StringBuilder();

        ErrorInfo ei = UtError.createErrorInfo(this.exc);

        sb.append("console.error('Error:',");
        sb.append(UtJson.toJson(ei.getText()));
        sb.append(");\n");

        sb.append("throw new Error(");
        sb.append(UtJson.toJson(ei.getText()));
        sb.append(");\n");

        return sb.toString();
    }

}
