package jandcode.core.dbm.sql;

import jandcode.commons.conf.*;

/**
 * Методы сервиса sql
 */
public interface ISqlService {

    /**
     * Создать {@link SqlText} для указанного текста sql.
     */
    SqlText createSqlText(String sql);

    /**
     * Создать {@link SqlText} по указанной конфигурации.
     * <p>
     * Как работает:
     * <p>
     * Если есть атрибут file, то берем этот файл.
     * <p>
     * Если файл указан, то либо берем его содержимое (любое расширение, кроме gsp),
     * либо генерируем текст по gsp, через шаблон, в котором доступно:
     * {@code this.getModel(), this.getConf(), this.getContext()}.
     * <p>
     * Если файл не указан, то берем текст из атрибута text.
     */
    SqlText createSqlText(Conf conf, Object context);

    /**
     * см. {@link ISqlService#createSqlText(jandcode.commons.conf.Conf, java.lang.Object)},
     * где context=null
     */
    default SqlText createSqlText(Conf conf) {
        return createSqlText(conf, null);
    }

    /**
     * Создать экземпляр {@link SqlBuilder}
     */
    SqlBuilder createSqlBuilder();

}
