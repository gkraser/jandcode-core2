package jandcode.core.web.virtfile.impl.filetype;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.web.virtfile.*;

public class FileTypeImpl extends BaseComp implements FileType {

    private String mime;
    private String type = TYPE_BIN;
    private boolean tml;

    public String getMime() {
        if (!UtString.empty(mime)) {
            return mime;
        }
        if (isText()) {
            return "text/" + getName();
        }
        return "application/octet-stream";
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        if (UtString.empty(type)) {
            type = TYPE_BIN;
        }
        this.type = type;
    }

    public boolean isTml() {
        return tml;
    }

    public void setTml(boolean tml) {
        this.tml = tml;
    }

    public boolean isPrivate() {
        return TYPE_PRIVATE.equals(getType());
    }

    public boolean isBin() {
        return TYPE_BIN.equals(getType());
    }

    public boolean isText() {
        return TYPE_TEXT.equals(getType());
    }

}
