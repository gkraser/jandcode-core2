package jandcode.jc.impl.just;

import jandcode.commons.*;
import jandcode.jc.*;

public class DirImpl implements Dir {

    private String _path = "";

    public DirImpl(String path) {
        _path = UtFile.abs(path);
    }

    public String getPath() {
        return _path;
    }

    public String join(CharSequence path) {
        if (path == null) {
            path = "";
        }
        return UtFile.abs(UtFile.join(getPath(), path.toString()));
    }

    public String call(CharSequence path) {
        return join(path);
    }

    public String getName() {
        return UtFile.filename(getPath());
    }

    //////

    public String toString() {
        return getPath();
    }

    public int hashCode() {
        return getPath().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof DirImpl) {
            return getPath().equals(((DirImpl) obj).getPath());
        } else if (obj instanceof CharSequence) {
            return getPath().equals(obj.toString());
        } else {
            return false;
        }
    }

    public Dir dir(String path) {
        return new DirImpl(join(path));
    }

}