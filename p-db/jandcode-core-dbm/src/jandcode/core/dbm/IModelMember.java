package jandcode.core.dbm;

/**
 * Интерфейс для объектов в контексте модели
 */
public interface IModelMember extends IModelLink {

    /**
     * Установить ссылку на модель.
     * Обычно не вызывается в коде.
     */
    void setModel(Model model);

}
