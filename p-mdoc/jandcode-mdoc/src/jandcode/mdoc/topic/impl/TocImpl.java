package jandcode.mdoc.topic.impl;

import jandcode.commons.*;
import jandcode.mdoc.topic.*;

import java.util.*;

public class TocImpl implements Toc {

    private Toc owner;
    private Topic topic;
    private String title;
    private String titleShort;
    private String section;
    private List<Toc> childs = new ArrayList<>();
    private String name;

    public Toc getOwner() {
        return owner;
    }

    public void setOwner(Toc owner) {
        this.owner = owner;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getTitle() {
        if (UtString.empty(title)) {
            if (topic != null) {
                return topic.getTitle();
            }
            return "";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        if (UtString.empty(this.titleShort)) {
            return getTitle();
        }
        return titleShort;
    }

    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String s) {
        this.section = s;
    }

    public Toc findBySection(String section) {
        if (UtString.empty(section)) {
            return null;
        }
        return findBySection_internal(this, section);
    }

    private Toc findBySection_internal(Toc toc, String section) {
        for (Toc t : toc.getChilds()) {
            if (t.getSection() != null && section.equals(t.getSection())) {
                return t;
            }
            Toc t1 = findBySection_internal(t, section);
            if (t1 != null) {
                return t1;
            }
        }
        return null;
    }

    public List<Toc> getChilds() {
        return childs;
    }

    public Toc addChild(Toc child) {
        child.setOwner(this);
        this.childs.add(child);
        return child;
    }

    public Toc findByTopic(String topicId) {
        topicId = UtVDir.normalize(topicId);
        if (UtString.empty(topicId)) {
            return null;
        }
        if (this.getTopic() != null) {
            if (topicId.equals(this.getTopic().getId())) {
                return this;
            }
        }
        return findByTopic_internal(this, topicId);
    }

    private Toc findByTopic_internal(Toc toc, String topicId) {
        for (Toc t : toc.getChilds()) {
            if (t.getTopic() != null) {
                if (topicId.equals(t.getTopic().getId())) {
                    return t;
                }
            }
            Toc t1 = findByTopic_internal(t, topicId);
            if (t1 != null) {
                return t1;
            }
        }
        return null;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
