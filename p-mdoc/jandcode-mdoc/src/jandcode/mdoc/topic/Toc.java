package jandcode.mdoc.topic;

import java.util.*;

/**
 * Элемент содержания
 */
public interface Toc {

    /**
     * Владелец в дереве содержания
     */
    Toc getOwner();

    void setOwner(Toc owner);

    /**
     * Для какой статьи
     */
    Topic getTopic();

    void setTopic(Topic topic);

    /**
     * Заголовок
     */
    String getTitle();

    void setTitle(String s);

    /**
     * Заголовок краткий
     */
    String getTitleShort();

    void setTitleShort(String s);

    /**
     * Секция внутри статьи
     */
    String getSection();

    void setSection(String s);

    /**
     * Найти элемент содержания с указанной секцией.
     * Ищется по всем дочерним в глубь.
     *
     * @return null, если не найдено
     */
    Toc findBySection(String section);

    /**
     * Дочерние элементы.
     * Добавлять в этот список нужно через addChild, иначе owner не проставится.
     */
    List<Toc> getChilds();

    /**
     * Добавить дочерний элемент
     */
    Toc addChild(Toc child);

    /**
     * Найти элемент содержания,ссылающийся на указанную статью.
     */
    Toc findByTopic(String topicId);

    /**
     * Произволное имя элемента содержания.
     * Пожет отсуствовать. Если нет, пустая строка.
     */
    String getName();

    void setName(String name);

}
