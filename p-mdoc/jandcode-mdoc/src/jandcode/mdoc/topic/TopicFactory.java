package jandcode.mdoc.topic;

import jandcode.mdoc.*;
import jandcode.mdoc.source.*;

/**
 * Фабрика статей
 */
public interface TopicFactory {

    /**
     * По исходному файлу создает статью
     *
     * @param sourceFile из какого файла
     * @param doc        в рамках какого документа
     */
    Topic createTopic(SourceFile sourceFile, Doc doc);

}
