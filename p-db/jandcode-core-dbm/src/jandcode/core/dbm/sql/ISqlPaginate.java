package jandcode.core.dbm.sql;

public interface ISqlPaginate {

    String paginate(String srcSql, long offset, long limit);

    String paginate(String srcSql, String paramsPrefix);

}
