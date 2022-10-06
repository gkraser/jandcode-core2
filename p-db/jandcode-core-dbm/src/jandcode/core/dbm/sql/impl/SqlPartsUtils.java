package jandcode.core.dbm.sql.impl;


import jandcode.commons.*;
import jandcode.commons.error.*;

import java.util.*;
import java.util.regex.*;

/**
 * Утилиты для замены некоторых частей sql
 */
public class SqlPartsUtils {

    protected static Pattern P_SELECT_A = Pattern.compile("/\\*\\*/\\s*?select\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_FROM_A = Pattern.compile("/\\*\\*/\\s*?from\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_SELECT = Pattern.compile("\\bselect\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_FROM = Pattern.compile("\\bfrom\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    protected static Pattern P_WHERE = Pattern.compile("\\bwhere\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_WHERE_A = Pattern.compile("/\\*\\*/\\s*?where\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_WHERE_PLACE = Pattern.compile("/\\*where\\*/", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    protected static Pattern P_ORDERBY = Pattern.compile("order\\s+by\\s+.*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_ORDERBY_A = Pattern.compile("order\\s+by\\s+.*/\\*end\\*/", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_ORDERBY_B = Pattern.compile("/\\*\\*/\\s*?order\\s+by\\s+.*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    protected static Pattern P_ORDERBY_C = Pattern.compile("/\\*\\*/\\s*?order\\s+by\\s+.*/\\*end\\*/", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    /**
     * Заменить where
     *
     * @param sql        исходный sql
     * @param whereName  имя where, по умолчанию 'default'
     * @param whereTexts условия, между ними ставится 'and'
     */
    public static String replaceWhere(String sql, String whereName, List<String> whereTexts) {
        if (whereTexts == null || whereTexts.size() == 0) {
            return sql;
        }
        //
        StringBuilder sb = new StringBuilder();
        for (String whereText : whereTexts) {
            if (sb.length() > 0) {
                sb.append(" and ");
            }
            sb.append(whereText);
        }
        String where = sb.toString();
        //
        if (UtString.empty(whereName)) {
            whereName = "default";
        }
        //
        Matcher m;

        Pattern NAMED_WHERE = Pattern.compile("\\/\\*where\\:" + whereName + "\\*\\/", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        m = NAMED_WHERE.matcher(sql);
        if (m.find()) {
            return m.replaceFirst("and " + where);
        }
        if ("default".equals(whereName)) {
            m = P_WHERE_PLACE.matcher(sql);
            if (m.find()) {
                return m.replaceFirst("and " + where);
            }
            //
            m = P_WHERE_A.matcher(sql);
            if (m.find()) {
                return m.replaceFirst("where " + where + " and");
            }
            //
            m = P_WHERE.matcher(sql);
            if (m.find()) {
                return m.replaceFirst("where " + where + " and");
            }
            throw new XError("Не найдено место для вставки условия where");
        }
        return sql;
    }

    /**
     * Заменить select
     *
     * @param text   на что
     * @param append true - полностью текст заменяется, false - добавляется текст
     */
    public static String replaceSelect(String sql, String text, boolean append) {
        if (UtString.empty(text)) {
            return sql;
        }

        if (append) {
            Matcher m = P_SELECT_A.matcher(sql);
            if (m.find()) {
                return m.replaceFirst("select " + text + ",");
            }
            m = P_SELECT.matcher(sql);
            if (m.find()) {
                return m.replaceFirst("select " + text + ",");
            }
            throw new XError("В sql нет select");
        } else {
            Pattern p1, p2;
            //
            if (P_SELECT_A.matcher(sql).find()) {
                p1 = P_SELECT_A;
            } else if (P_SELECT.matcher(sql).find()) {
                p1 = P_SELECT;
            } else {
                throw new XError("В sql нет select");
            }
            //
            if (P_FROM_A.matcher(sql).find()) {
                p2 = P_FROM_A;
            } else if (P_FROM.matcher(sql).find()) {
                p2 = P_FROM;
            } else {
                throw new XError(UtLang.t("В sql нет from"));
            }
            //
            Pattern pat = Pattern.compile(p1.pattern() + ".*?" + p2.pattern(), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            Matcher m = pat.matcher(sql);
            if (!m.find()) {
                throw new XError(UtLang.t("Не найдена часть sql: {0}", pat.pattern()));
            }
            return m.replaceFirst("select " + text + " from");

        }
    }

    /**
     * Заменить order by
     *
     * @param text на что. Если пусто - order by удаляется
     */
    public static String replaceOrderBy(String sql, String text) {
        Matcher m;

        m = P_ORDERBY_C.matcher(sql);
        if (!m.find()) {
            m = P_ORDERBY_B.matcher(sql);
            if (!m.find()) {
                m = P_ORDERBY_A.matcher(sql);
                if (!m.find()) {
                    m = P_ORDERBY.matcher(sql);
                    if (!m.find()) {
                        return sql;
                    }
                }
            }
        }
        if (UtString.empty(text)) {
            return m.replaceFirst("");
        } else {
            return m.replaceFirst("order by " + text);
        }

    }

}
