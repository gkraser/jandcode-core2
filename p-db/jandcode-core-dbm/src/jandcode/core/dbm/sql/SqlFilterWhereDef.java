package jandcode.core.dbm.sql;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Определение зарегистрированного SqlFilterWhere
 */
public interface SqlFilterWhereDef extends Comp, IConfLink {

    SqlFilterWhere createInst();

}
