package jandcode.db;

import jandcode.core.*;
import jandcode.commons.conf.*;

/**
 * Объявление DbSource
 */
public interface DbSourceDef extends Comp, IConfLink {

    /**
     * Оригинальная структура conf для этого dbsource
     */
    Conf getConf();

    /**
     * Создать новый экземпляр
     */
    DbSource createInst();

    /**
     * Вернуть кешированный экземпляр
     */
    DbSource getInst();

}
