package jandcode.commons.event;

/**
 * Интерфейс обработчика событий
 */
public interface EventHandler<T extends Event> {

    /**
     * Обработать событие
     */
    void handleEvent(T e) throws Exception;

}

