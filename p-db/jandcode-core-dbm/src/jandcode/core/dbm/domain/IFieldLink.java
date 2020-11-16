package jandcode.core.dbm.domain;

/**
 * Интерфейс для объектов, которые знают про свое поле домена
 */
public interface IFieldLink {

    /**
     * Поле домена
     */
    Field getField();

}
