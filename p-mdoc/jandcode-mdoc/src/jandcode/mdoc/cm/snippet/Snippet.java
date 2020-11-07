package jandcode.mdoc.cm.snippet;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.mdoc.source.*;

import java.util.regex.*;

public class Snippet {

    public static final String CONTENT = "content";
    public static final String CONTENT_ALL = "content-all";

    private NamedList<SnippetPart> parts = new DefaultNamedList<>();
    private String lang;

    public void configure(SourceFile f) {
        configure(f.getText(), UtFile.ext(f.getPath()));
    }

    public void configure(String text, String lang) {
        this.lang = lang;
        addPart(CONTENT, text);
        addPart(CONTENT_ALL, text);
        makeParts(text);
    }

    protected void makeParts(String text) {
    }

    public SnippetPart getPart(String name) {
        if (UtString.empty(name)) {
            name = CONTENT;
        }
        return this.parts.find(name);
    }

    protected void addPart(String name, String text) {
        addPart(name, text, this.lang);
    }

    protected void addPart(String name, String text, String lang) {
        SnippetPart p = new SnippetPart(name, text, lang);
        parts.add(p);
    }

    /**
     * Все части
     */
    public NamedList<SnippetPart> getParts() {
        return parts;
    }

    public String getLang() {
        return lang;
    }

    ////// utils

    protected String replaceAll(String txt, String pattern, String replace) {
        Pattern p = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(txt);
        if (m.find()) {
            txt = m.replaceAll(replace);
        }
        return txt;
    }

}
