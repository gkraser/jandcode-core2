package jandcode.core.db.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.db.*;
import org.slf4j.*;

import java.util.*;

/**
 * Утилиты для логгирования запроса
 */
public class QueryLogger {

    protected static Logger log = LoggerFactory.getLogger(DbQuery.class);

    private DbQueryImpl query;
    private long startTime;

    public QueryLogger(DbQuery query) {
        this.query = (DbQueryImpl) query;
    }

    public void logStart() {
        if (!log.isInfoEnabled()) {
            return;
        }
        startTime = System.currentTimeMillis();
        log.info(buildDebugInfo());
    }

    public void logStop() {
        if (!log.isInfoEnabled()) {
            return;
        }

        long tmms = System.currentTimeMillis() - startTime;
        String tm = String.format("%.3f sec.", tmms / 1000.0);

        log.info(tm);
    }

    /**
     * Маркировать ошибку для отладки
     */
    public void markError(Exception e) throws Exception {
        throw new XErrorMark(e, buildDebugInfo());
    }

    //////


    /**
     * Строит информацию для отладки и логирования
     */
    protected String buildDebugInfo() {
        String d = "~";
        int len = 45;

        String sql = query.getSql();
        List<String> paramNames = query.getSqlPreparedParams();
        IVariantNamed params = query.getParams();

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(UtString.delim("SQL", d, len)).append("\n");
        sb.append(UtString.normalizeIndent(sql)).append("\n");

        if (paramNames != null && paramNames.size() > 0) {
            sb.append(UtString.delim("Params", d, len)).append("\n");
            for (String pn : paramNames) {
                if (params.isNull(pn)) {
                    sb.append(pn).append("=<NULL>\n");
                } else if (params.getDataType(pn) == VariantDataType.BLOB) {
                    sb.append(pn).append("=").append("<BLOB>").append("\n");
                } else {
                    sb.append(pn).append("=").append(params.getString(pn)).append("\n");
                }
            }
        }
        sb.append(UtString.delim("", d, len));

        return sb.toString();
    }

}
