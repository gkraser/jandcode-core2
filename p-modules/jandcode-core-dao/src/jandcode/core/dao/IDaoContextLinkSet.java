package jandcode.core.dao;

/**
 * Интерфейс для классов, которые могут работать в контексте dao
 */
public interface IDaoContextLinkSet {

    /**
     * Установить контекст dao
     */
    void setContext(DaoContext context);

}
