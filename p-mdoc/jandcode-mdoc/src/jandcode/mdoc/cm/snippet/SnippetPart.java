package jandcode.mdoc.cm.snippet;

import jandcode.commons.named.*;

public class SnippetPart implements INamed {

    private String name;
    private String text;
    private String lang;

    public SnippetPart(String name, String text, String lang) {
        this.name = name;
        this.text = text;
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLang() {
        return lang;
    }
}
