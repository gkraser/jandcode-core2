package jandcode.commons.error;

/**
 * Информация о конкретной ошибке валидации
 */
public interface ValidateErrorInfo {

    /**
     * Сообщение об ошибке
     */
    String getMessage();

    /**
     * Имя поля, если ошибка привязана к полю
     */
    String getField();

    /**
     * Связанный объект данных
     */
    Object getData();

}
