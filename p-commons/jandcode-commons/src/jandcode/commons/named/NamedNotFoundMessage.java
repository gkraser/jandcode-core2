package jandcode.commons.named;

/**
 * Интерфейс для генераторов сообщений о том, что поименнованный элемент не
 * найден в хранилище.
 */
public interface NamedNotFoundMessage {

    /**
     * Метод должен сгенерировать сообщение о том, что поименнованный элемент
     * с указанным именем не найден.
     */
    String makeNotFoundMessage(String name);

}
