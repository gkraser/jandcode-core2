package jandcode.mdoc;

import jandcode.core.*;
import jandcode.mdoc.cfg.*;
import jandcode.mdoc.topic.*;

/**
 * Сервис mdoc
 */
public interface MDocService extends Comp {

    TopicService getTopicService();

    DocCfgService getDocCfgService();

    /**
     * Создать документ по конфигурации
     */
    Doc createDocument(DocCfg cfg) throws Exception;

}
