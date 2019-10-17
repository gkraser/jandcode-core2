package jandcode.mdoc.flexmark.mdtopic.impl;

import jandcode.mdoc.flexmark.mdtopic.*;

import java.util.*;

public class MdTocImpl implements MdToc {

    private String title;
    private String id;
    private List<MdToc> childs = new ArrayList<>();
    private int nodeLevel;

    public MdTocImpl(String id, String title, int nodeLevel) {
        this.id = id;
        this.title = title;
        this.nodeLevel = nodeLevel;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public List<MdToc> getChilds() {
        return childs;
    }

    public int getNodeLevel() {
        return nodeLevel;
    }
}
