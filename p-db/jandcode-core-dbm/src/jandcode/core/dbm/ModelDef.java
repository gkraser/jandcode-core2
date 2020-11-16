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
     * Проверка: определен ли указанный объект в этой модели непосредственно
     *
     * @param dbtype для какого типа базы данных
     * @param idn    идентификатор. Обычно conf-путь для объекта.
     * @return true, если определен в этой модели, но это не значит, что это первыя модель,
     * которая определила объект. Возможно его просто перекрыли
     */
    boolean isDefinedHere(String dbtype, String idn);

    /**
     * Проверка: определен ли указанный объект в этой модели для использования
     * в структуре базы данных. Такой объект должен быть определен в одной из моделей,
     * которые входят в структуру базы данных этой модели.
     * Параметры см. в {@link ModelDef#isDefinedHere(java.lang.String, java.lang.String)}.
     */
    boolean isDefinedForDbStruct(String dbtype, String idn);

    /**
     * Режим структуры базы данных
     */
    DbMode getDbMode();

    /**
     * В каком модуле определена модель
     */
    ModuleInst getModule();

}
