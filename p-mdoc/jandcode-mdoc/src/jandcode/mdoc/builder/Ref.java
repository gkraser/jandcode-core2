package jandcode.mdoc.builder;

import jandcode.commons.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

/**
 * Ссылка, распознанная по тексту внутри файла
 */
public class Ref {

    private Topic topic;
    private SourceFile sourceFile;
    private String section;

    /**
     * Ссылка на эту статью
     */
    public Ref(Topic topic, String section) {
        this.topic = topic;
        this.section = section;
        this.sourceFile = topic.getSourceFile();
    }

    /**
     * Ссылка на этот файл
     */
    public Ref(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Текст ссылки.
     * Если статья - заголовок статьи.
     * Если сеуция статьи - заголовок секции.
     * Если файл - путь файла.
     * Если пустая - NoText
     */
    public String getText() {
        if (topic != null) {
            if (!UtString.empty(section)) {
                Toc toc = topic.getToc().findBySection(section);
                if (toc != null) {
                    return toc.getTitle();
                }
            }
            return topic.getTitle();
        }
        if (sourceFile != null) {
            return sourceFile.getPath();
        }
        return "NoText";
    }

    /**
     * Ссылка-маркер {~ref:PATH}
     */
    public String getRef() {
        String s = "";
        if (topic != null) {
            s = topic.getId();
            if (!UtString.empty(section)) {
                s += "#" + section;
            }
        } else if (sourceFile != null) {
            s = sourceFile.getPath();
        }
        // специально оформленная ссылка для последующего url-rewrite
        return MDocConsts.MARK_REF_PREFIX + s + MDocConsts.MARK_REF_SUFFIX;
    }

    /**
     * Статья, на которую ссылка. Может быт null.
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * Файл, на который ссылка.
     * Если getTopic()!=null, то здесь исходный файл статьи.
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }
}
