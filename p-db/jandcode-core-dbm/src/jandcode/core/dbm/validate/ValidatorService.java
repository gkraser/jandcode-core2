package jandcode.core.dbm.validate;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;

import java.util.*;

/**
 * Сервис поддержки валидаторов
 */
public interface ValidatorService extends Comp, IModelMember {

    /**
     * Зарегистрированные валидаторы
     */
    NamedList<ValidatorDef> getValidators();

    /**
     * Создать определение валидатора по конфигурации
     */
    ValidatorDef createValidatorDef(Conf conf);

    /**
     * Создать контекст валидации
     *
     * @param mdb   для какой mdb
     * @param data  какие данные будем проверять
     * @param attrs атрибуты контекста
     * @return экземпляр контекста
     */
    ValidatorContext createValidatorContext(Mdb mdb, Object data, Map attrs);

    /**
     * Выполнить валидатор
     *
     * @param mdb       для какой mdb
     * @param data      для каких данных
     * @param attrs     атрибуты контекста валидатора
     * @param validator экземплар валидатора
     * @return false, если были ошибки валидации
     */
    boolean validatorExec(Mdb mdb, Object data, Validator validator, Map attrs) throws Exception;

    /**
     * Выполнить валидатор
     *
     * @param mdb          для какой mdb
     * @param data         для каких данных
     * @param validatorDef описание валидатора
     * @param attrs        атрибуты контекста валидатора
     * @return false, если были ошибки валидации
     */
    boolean validatorExec(Mdb mdb, Object data, ValidatorDef validatorDef, Map attrs) throws Exception;

    /**
     * Выполнить валидатор
     *
     * @param mdb   для какой mdb
     * @param data  для каких данных
     * @param name  имя зарегистрированного валидатора
     * @param attrs атрибуты контекста валидатора
     * @return false, если были ошибки валидации
     */
    boolean validatorExec(Mdb mdb, Object data, String name, Map attrs) throws Exception;

}
