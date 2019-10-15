package jandcode.core;

/**
 * Интерфейс для сервисов приложения, которые хотят
 * выполнить некий код после того, как приложение загружено и проинициализировано.
 */
public interface IAppLoaded {

    void appLoaded() throws Exception;

}
