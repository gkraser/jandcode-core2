package jandcode.core;

/**
 * Интерфейс для сервисов приложения, которые должны
 * реагировать на вызов метода {@link App#startup()}.
 */
public interface IAppStartup {

    void appStartup() throws Exception;

}
