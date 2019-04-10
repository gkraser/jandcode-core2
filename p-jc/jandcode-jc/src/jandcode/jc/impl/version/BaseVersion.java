package jandcode.jc.impl.version;

import jandcode.jc.*;

public abstract class BaseVersion implements IVersion {

    public boolean isDummy() {
        return false;
    }

    public String toString() {
        return this.getText();
    }

}
