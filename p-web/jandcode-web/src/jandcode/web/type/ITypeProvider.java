package jandcode.web.type;

import java.util.*;

/**
 * Провайдер для TypeDef.
 * Его цель - зарегистрировать набор TypeDef по его правилам.
 */
public interface ITypeProvider {

    /**
     * Загрузить types.
     * Возвращает null, если нет доступных объектов для загрузки.
     */
    List<TypeDef> loadTypes() throws Exception;

}
