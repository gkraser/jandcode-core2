package jandcode.core.dbm.sql.impl;

import jandcode.commons.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

public class SqlPaginateImpl extends BaseModelMember implements SqlPaginate {

    public String paginate(String srcSql, long offset, long limit) {
        return makePaginateSql(srcSql, UtString.toString(offset), UtString.toString(limit));
    }

    public String paginate(String srcSql, String paramsPrefix) {
        String p_offset = "offset";
        String p_limit = "limit";
        if (!UtString.empty(paramsPrefix)) {
            p_offset = paramsPrefix + p_offset;
            p_limit = paramsPrefix + p_limit;
        }
        return makePaginateSql(srcSql, ":" + p_offset, ":" + p_limit);
    }

    //////

    protected String makePaginateSql(String srcSql, String offsetText, String limitText) {
        if (getModel().getDbType().equals("oracle")) {
            return makePaginateSql_oracle(srcSql, offsetText, limitText);
        } else {
            return makePaginateSql_std(srcSql, offsetText, limitText);
        }
    }

    protected String makePaginateSql_std(String srcSql, String offsetText, String limitText) {
        return srcSql + " limit " + limitText + " offset " + offsetText;
    }

    protected String makePaginateSql_oracle(String srcSql, String offsetText, String limitText) {
        return "select * from ( select zzz___all.*, rownum as zzz___n from (\n\n" +
                srcSql +
                "\n\n) zzz___all ) where zzz___n>" + offsetText +
                " and zzz___n<=" + offsetText + "+" + limitText;
    }


}
