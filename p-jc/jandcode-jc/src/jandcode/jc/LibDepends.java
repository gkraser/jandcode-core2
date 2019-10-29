package jandcode.jc;

import jandcode.commons.named.*;

/**
 * Зависимости от библиотек.
 * <p>
 * Зависимости объединяются в группы.
 * Группы: prod, dev, jc - присутсвуют всегда.
 */
public interface LibDepends {

    /**
     * Получить группу по имени.
     * Если не существует - ошибка.
     */
    LibDependsGroup getGroup(String name);

    /**
     * Добавить произвольную группу
     *
     * @param name имя группы
     * @return если группа не существует, то создается. Иначе возвращается существующая
     */
    LibDependsGroup addGroup(String name);

    /**
     * Список всех групп
     */
    NamedList<LibDependsGroup> getGroups();

    /**
     * Запускает проверку: все имена библиотек должны быть библиотеками
     */
    void validate();

    /**
     * Группа: prod.
     * Библиотеки в этой группе обязательны и используются в production.
     */
    LibDependsGroup getProd();

    /**
     * Группа: dev.
     * Библиотеки в этой группе используются только при разработке конкретного модуля,
     * например в тестах. Не используются вне модуля.
     */
    LibDependsGroup getDev();

    /**
     * Группа: all.
     * Виртуальная группа. Содержит все библиотеки из всех групп.
     */
    LibDependsGroup getAll();

}
