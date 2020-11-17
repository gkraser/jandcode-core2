package jandcode.core.dao;

/**
 * Фильтр для процесса исполнения dao
 */
public interface DaoFilter {

    /**
     * Вызывается для каждого типа фильтра по необходимости
     *
     * @param type тип фильтра
     * @param ctx  контекст dao
     */
    void execDaoFilter(DaoFilterType type, DaoContext ctx) throws Exception;

}
