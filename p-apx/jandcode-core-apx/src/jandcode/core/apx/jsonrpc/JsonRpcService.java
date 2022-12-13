package jandcode.core.apx.jsonrpc;

import jandcode.core.*;
import jandcode.core.dao.*;

/**
 * Сервис для поддержки jsonrpc
 */
public interface JsonRpcService extends Comp {

    /**
     * Конвертировать результат выполнения dao для передачи клиенту
     *
     * @param daoContext контекст исполнения dao
     * @param inst       результат выполнения dao
     * @return объект, которые будет передан клиенту
     */
    Object convertForClient(DaoContext daoContext, Object inst);

}
