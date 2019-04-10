package jandcode.mdoc;

import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

/**
 * Утилиты для Doc
 */
public class DocUtils {

    /**
     * Создать статью на лету из текста без физического файла.
     *
     * @param doc  для какого документа
     * @param path виртуальный путь для файла статьи
     * @param text текст файла статьи
     * @return статья
     */
    public static Topic createTopic(Doc doc, String path, String text) {
        SourceFile f = new TextSourceFile(path, text);
        Topic topic = doc.createTopic(f);
        return topic;
    }

    /**
     * Создать статью на лету из текста без физического файла
     * и добавить в документ.
     *
     * @param doc  для какого документа
     * @param path виртуальный путь для файла статьи
     * @param text текст файла статьи
     * @return статья
     */
    public static Topic addTopic(Doc doc, String path, String text) {
        Topic topic = createTopic(doc, path, text);
        doc.getSourceFiles().add(topic.getSourceFile());
        doc.getTopics().add(topic);
        return topic;
    }

}
