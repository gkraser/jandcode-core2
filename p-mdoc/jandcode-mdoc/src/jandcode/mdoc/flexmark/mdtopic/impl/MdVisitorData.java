package jandcode.mdoc.flexmark.mdtopic.impl;

import jandcode.mdoc.flexmark.mdtopic.*;

import java.util.*;

/**
 * Данные, которые собирает MdVisitor
 */
public class MdVisitorData {

    private MdToc toc = new MdTocImpl("ROOT", "RootTitle", 0);
    private String title;
    private Map<String, String> props = new LinkedHashMap<>();

    public MdToc getToc() {
        return toc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Свойства из front matter
     */
    public Map<String, String> getProps() {
        return props;
    }

}
