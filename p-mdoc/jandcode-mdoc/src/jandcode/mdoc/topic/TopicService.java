package jandcode.mdoc.topic;

import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;

/**
 * Сервис для статей
 */
public interface TopicService extends Comp {

    /**
     * Найти фабрику статей для файла
     *
     * @param sourceFile для какого файла
     * @param doc        в контексте какого документа
     * @return null, если для такого файла не предусмотрено фабрики статьи
     */
    TopicFactory findTopicFactory(SourceFile sourceFile, Doc doc);

}
