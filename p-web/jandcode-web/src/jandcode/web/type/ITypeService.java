package jandcode.web.type;

import jandcode.commons.named.*;

/**
 * Интерфейс сервиса для хранения и управление type
 */
public interface ITypeService {

    /**
     * Зарегистрированные type.
     */
    NamedList<TypeDef> getTypes();

    /**
     * Получить тип, ассоциированный с указанным классом.
     * Если не найден - возвращается null.
     */
    TypeDef findType(Class cls);

}
