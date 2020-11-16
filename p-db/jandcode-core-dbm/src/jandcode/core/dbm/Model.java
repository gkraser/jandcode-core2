package jandcode.core.dbm;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.db.*;

/**
 * Модель.
 */
public interface Model extends Comp, BeanFactoryOwner, IConfLink,
        IModelDbService {

    /**
     * Кто создал эту модель
     */
    ModelDef getModelDef();

    /**
     * Проверка: определен ли указанный объект в этой модели для использования
     * в структуре базы данных. Такой объект должен быть определен в одной из моделей,
     * которые входят в структуру базы данных этой модели.
     * Параметры см. в {@link ModelDef#isDefinedHere(java.lang.String, java.lang.String)}.
     */
    boolean isDefinedForDbStruct(String idn);

}
