package jandcode.mdoc.topic.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

public class TopicImpl implements Topic {

    private String id;
    private SourceFile sourceFile;
    private String body;
    private String title;
    private String titleShort;
    private Toc toc;
    private IVariantMap props = new VariantMap();

    public TopicImpl(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
        this.id = UtFile.removeExt(sourceFile.getPath());
        this.toc = new TocImpl();
        this.toc.setTopic(this);
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        if (UtString.empty(title)) {
            if (sourceFile != null) {
                return UtFile.removeExt(UtFile.filename(sourceFile.getPath()));
            } else {
                return "No title";
            }
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

    public Toc getToc() {
        return toc;
    }

    public IVariantMap getProps() {
        return props;
    }

    public void reload() {
        throw new XError("unsupported reload");
    }
}
