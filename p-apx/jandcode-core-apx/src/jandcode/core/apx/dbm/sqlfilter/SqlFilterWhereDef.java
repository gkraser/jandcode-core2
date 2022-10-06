package jandcode.core.apx.dbm.sqlfilter;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Определение зарегистрированного SqlFilterWhere
 */
public interface SqlFilterWhereDef extends Comp, IConfLink {

    SqlFilterWhere createInst();

}
