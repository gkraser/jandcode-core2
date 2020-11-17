package jandcode.core.dbm;

import jandcode.commons.conf.*;
import jandcode.core.*;

import java.util.*;

/**
 * Объявление модели
 */
public interface ModelDef extends Comp, IConfLink {

    /**
     * Создать экземпляр модели
     */
    Model createInst();

    /**
     * Возвращает кешированный экземпляр модели
     */
    Model getInst();

    /**
     * Конфигурация модели в виде conf, как она описана в app.cfx.
     * Кешированный объект.
     */
    Conf getConf();

    /**
     * Объединенная conf модели. Включает в наложение всех моделей,
     * включенную в данную и саму модель.
     * Кроме этого эта conf была обработана в соответствии с dbtype данной модели.
     * Кешированный объект.
     */
    Conf getJoinConf();

    /**
     * Список моделей, которые включены в данную
     */
    List<ModelDef> getIncludedModels();

    /**
     * true - является экземпляром, а не описанием модели.
     */
    boolean isInstance();

    /**
     * Экземпляром какой модели является.
     * Если эта модель экземпляр (isInstance()==true), то тут та модель,
     * чьим экземпляром является.
     * Иначе - ссылка на себя (является экземпляром самой себя).
     */
    ModelDef getInstanceOf();

    /**
     * В каком модуле определена модель
     */
    ModuleInst getModule();

}
