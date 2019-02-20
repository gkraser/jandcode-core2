package jandcode.commons.idgen;

/**
 * Генератор уникальных id
 */
public interface IdGenerator {

    /**
     * Следующая уникальная в пределах объекта id
     */
    String nextId();

    /**
     * Следующая уникальная в пределах объекта id с указанным префиксом
     */
    String nextId(String prefix);

}
