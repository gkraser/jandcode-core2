package jandcode.commons.event;

/**
 * Шина событий
 */
public interface EventBus {

    /**
     * Подписаться на событие
     *
     * @param eventClass класс события
     * @param handler    обработчик события
     */
    <T extends Event> void onEvent(Class<T> eventClass, EventHandler<T> handler);

    /**
     * Отписаться от события
     *
     * @param eventClass класс события
     * @param handler    обработчик события
     */
    <T extends Event> void unEvent(Class<T> eventClass, EventHandler<T> handler);

    /**
     * Инициировать событие
     *
     * @param e событие
     */
    void fireEvent(Event e);

}
