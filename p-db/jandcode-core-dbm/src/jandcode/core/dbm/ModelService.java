package jandcode.core.dbm;

import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Сервис моделей
 */
public interface ModelService extends Comp {

    /**
     * Зарегистрированные модели
     */
    NamedList<ModelDef> getModels();

    /**
     * Получить экземпляр модель по имени
     */
    Model getModel(String name);

    /**
     * Получить экземпляр модель с именем 'default'
     */
    Model getModel();

}
