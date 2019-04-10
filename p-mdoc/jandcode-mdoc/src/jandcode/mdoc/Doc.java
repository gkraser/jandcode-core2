package jandcode.mdoc;

import jandcode.core.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.cfg.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

/**
 * Документ
 */
public interface Doc extends IAppLink {

    /**
     * Конфигурация документа
     */
    DocCfg getCfg();

    /**
     * Режим работы документа
     */
    DocMode getMode();

    /**
     * Загрузить документ.
     */
    void load() throws Exception;

    /**
     * Все исходные файлы
     */
    SourceFileHolder getSourceFiles();

    /**
     * Все статьи
     */
    TopicHolder getTopics();

    /**
     * По исходному файлу создает статью
     *
     * @param sourceFile из какого файла
     * @return null, если по этому файлу создать статью не умеет
     */
    Topic createTopic(SourceFile sourceFile);

    /**
     * Создать builder для документа
     *
     * @param name имя builder
     */
    OutBuilder createBuilder(String name) throws Exception;

}
