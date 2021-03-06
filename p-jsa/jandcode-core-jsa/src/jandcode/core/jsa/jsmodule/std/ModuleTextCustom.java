package jandcode.core.jsa.jsmodule.std;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.jsa.jsmodule.*;

/**
 * Базовый реализатор для разных вариантов IModuleText для клиента
 */
public abstract class ModuleTextCustom extends BaseComp implements IModuleText {

    private String hash;
    private String text;

    /**
     * В этом методе нужно сформировать текст
     */
    protected abstract String makeText();

    public String getText() {
        if (text == null) {
            synchronized (this) {
                if (text == null) {
                    text = makeText();
                }
            }
        }
        return text;
    }

    public String getHash() {
        if (hash == null) {
            synchronized (this) {
                if (hash == null) {
                    hash = UtString.md5Str(getText());
                }
            }
        }
        return hash;
    }

}
