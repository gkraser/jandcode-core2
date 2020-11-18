package jandcode.core.dao;

import jandcode.commons.*;

/**
 * Правило для выявления daoInvoker
 */
public interface DaoHolderRule {

    /**
     * Маска.
     * см: {@link UtVDir#matchPath(java.lang.String, java.lang.String)}
     */
    String getMask();

    /**
     * Имя daoInvoker
     */
    String getInvoker();

}
