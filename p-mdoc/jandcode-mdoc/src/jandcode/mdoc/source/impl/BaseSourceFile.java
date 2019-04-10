package jandcode.mdoc.source.impl;

import jandcode.commons.*;
import jandcode.mdoc.source.*;

/**
 * Базовая реализация SourceFile.
 */
public abstract class BaseSourceFile implements SourceFile {

    private String path;

    public String getPath() {
        return path;
    }

    public String getRealPath() {
        return null;
    }

    protected void setPath(String path) {
        this.path = UtVDir.normalize(path);
    }

    public String toString() {
        return getPath();
    }

}
