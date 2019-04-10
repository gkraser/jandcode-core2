package jandcode.jc.impl.version;

import jandcode.commons.*;
import jandcode.jc.*;

/**
 * Реализация версии в виде текста
 */
public class TextVersion extends BaseVersion {

    private String version;

    public TextVersion(String version) {
        this.version = version;
    }

    public String getText() {
        return isDummy() ? JcConsts.VERSION_DEFAULT : this.version;
    }

    public boolean isDummy() {
        return UtString.empty(this.version);
    }
}
