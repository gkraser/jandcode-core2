package jandcode.mdoc.topic;

import java.util.*;

/**
 * Набор статей
 */
public interface TopicHolder extends Iterable<Topic> {

    /**
     * Добавить статью. Если статья с таким {@link Topic#getId()} уже
     * существует, она заменяется на новую.
     */
    void add(Topic topic);

    /**
     * Коллекция всех статей
     */
    Collection<Topic> getItems();

    /**
     * Найти статью по id или по имени файла со статьей.
     *
     * @return null, если не найдена
     */
    Topic find(String id);

    /**
     * Найти татью по id
     *
     * @return ошибка, если не найдена
     */
    Topic get(String id);

}
