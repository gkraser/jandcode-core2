package jandcode.core.web.filter;

import jandcode.core.web.*;

/**
 * Фильтр для процесса выполнения запроса.
 * Экземпляры фильтров существуют в приложении в единственном
 * экземпляре, которые создаются при запуске приложения, так что не стоит
 * использовать в них поля класса для временного хранения информации.
 * <p>
 * Если фильтру нужно как то передать информацию другому фильтру,
 * то для этого можно использованть параметры запроса {@link Request#getParams()} по
 * соглашению между фильтрами.
 */
public interface IFilter {

    /**
     * Выполнить фильтр
     */
    void execFilter(FilterType type, Request request) throws Exception;

}
