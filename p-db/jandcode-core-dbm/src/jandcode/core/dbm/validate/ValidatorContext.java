package jandcode.core.dbm.validate;

import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.mdb.*;

import java.util.*;

/**
 * Контекст работы валидатора.
 * <ul>
 *     <li>IVariantNamed - значение getRec().getValue(name)</li>
 *     <li>IVariant - значение getRec().getValue(getField())</li>
 * </ul>
 */
public interface ValidatorContext extends IValidateErrorsLink, ValidateErrors,
        IMdbLink, IModelLink, IVariantNamed, IVariant {

    /**
     * Произвольные атрибуты
     */
    IVariantMap getAttrs();

    /**
     * Валидируемое поле
     */
    String getField();

    /**
     * Данные для проверки
     */
    Object getData();

    /**
     * Данные для проверки в виде {@link IVariantNamed}
     */
    IVariantNamed getRec();

    /**
     * Заголовок для формирования ошибок валидации
     */
    String getTitle();

    /**
     * Домен записи. Можно явно указывать в атрибутах.
     * Если явно не указан, то берется из store, если имеется.
     *
     * @return null, если домен не определен.
     */
    Domain getDomain();

    /**
     * Домен записи. Можно явно указывать в атрибутах.
     * Если явно не указан, то берется из store, если имеется.
     *
     * @return ошибка, если домен не определен.
     */
    Domain getDomain(boolean required);

    /**
     * Выполнить указанный валидатор.
     * Атрибуты attrs дополняют и перекрывают текущие атрибуты контекста.
     * mdb и data - такие же, как и у контекста.
     *
     * @param validatorName какой валидатор
     * @param attrs         атрибуты валидатора
     * @return false, если были ошибки валидации
     */
    boolean validate(String validatorName, Map attrs) throws Exception;

    /**
     * Выполнить указанный валидатор.
     * attrs - текущие атрибуты контекста.
     * mdb и data - такие же, как и у контекста.
     *
     * @param validatorName какой валидатор
     * @return false, если были ошибки валидации
     */
    default boolean validate(String validatorName) throws Exception {
        return validate(validatorName, null);
    }

    /**
     * Выполнить указанный валидатор.
     * Атрибуты attrs дополняют и перекрывают текущие атрибуты контекста.
     * mdb и data - такие же, как и у контекста.
     *
     * @param validatorDef какой валидатор
     * @param attrs        атрибуты валидатора
     * @return false, если были ошибки валидации
     */
    boolean validate(ValidatorDef validatorDef, Map attrs) throws Exception;

    /**
     * Выполнить указанный валидатор.
     * attrs - текущие атрибуты контекста.
     * mdb и data - такие же, как и у контекста.
     *
     * @param validatorDef какой валидатор
     * @return false, если были ошибки валидации
     */
    default boolean validate(ValidatorDef validatorDef) throws Exception {
        return validate(validatorDef, null);
    }

}
