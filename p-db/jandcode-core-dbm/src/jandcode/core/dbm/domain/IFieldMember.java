package jandcode.core.dbm.domain;

/**
 * Интерфейс для объектов в контексте поля домена
 */
public interface IFieldMember extends IFieldLink {

    /**
     * Поле домена
     */
    void setField(Field field);

}
