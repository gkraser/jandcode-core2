package jandcode.core.dbm.sql;

/**
 * Методы для пагинации
 */
public interface SqlPaginate {

    /**
     * Сделать запрос с пагинацией.
     * Значения offset и limit встраиваются в тело запроса.
     *
     * @param srcSql исходный запрос
     * @param offset смещение (с 0)
     * @param limit  сколько записей нужно
     * @return новый текст запроса с пагинацией
     */
    String paginate(String srcSql, long offset, long limit);

    /**
     * Сделать запрос с пагинацией.
     * Значения offset и limit встраиваются в тело запроса как параметры
     * с префиксом paramsPrefix.
     *
     * @param srcSql       исходный запрос
     * @param paramsPrefix префикс имен параметров offset и limit. Если значение пустое,
     *                     то параметры будут иметь имена 'offset' и 'limit'
     * @return новый текст запроса с пагинацией
     */
    String paginate(String srcSql, String paramsPrefix);


    /**
     * Сделать запрос с пагинацией.
     * Значения offset и limit встраиваются в тело запроса как
     * параметры 'offset' и 'limit'.
     *
     * @param srcSql исходный запрос
     * @return новый текст запроса с пагинацией
     */
    default String paginate(String srcSql) {
        return paginate(srcSql, null);
    }

}
