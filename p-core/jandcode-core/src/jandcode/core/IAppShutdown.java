package jandcode.core;

/**
 * Интерфейс для сервисов приложения, которые должны
 * реагировать на вызов метода {@link App#shutdown()}.
 */
public interface IAppShutdown {

    void appShutdown() throws Exception;

}
