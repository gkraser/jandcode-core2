package jandcode.core.dbm.dao;

import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Сервис dao для модели
 */
public interface ModelDaoService extends Comp, IModelMember {

    <A> A createDao(Class<A> cls);

}
