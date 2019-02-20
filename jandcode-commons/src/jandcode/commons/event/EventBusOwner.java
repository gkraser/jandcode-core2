package jandcode.commons.event;

/**
 * Владелец шины событий
 */
public interface EventBusOwner extends EventBus {

    /**
     * Шина событий
     */
    EventBus getEventBus();

    //////

    default <T extends Event> void onEvent(Class<T> eventClass, EventHandler<T> handler) {
        getEventBus().onEvent(eventClass, handler);
    }

    default <T extends Event> void unEvent(Class<T> eventClass, EventHandler<T> handler) {
        getEventBus().unEvent(eventClass, handler);
    }

    default void fireEvent(Event e) {
        getEventBus().fireEvent(e);
    }

}
