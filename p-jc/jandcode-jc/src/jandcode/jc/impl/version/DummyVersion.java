package jandcode.jc.impl.version;

import jandcode.jc.*;

/**
 * Версия по умолчанию.
 */
public class DummyVersion extends BaseVersion {

    public String getText() {
        return JcConsts.VERSION_DEFAULT;
    }

    public boolean isDummy() {
        return true;
    }

}
