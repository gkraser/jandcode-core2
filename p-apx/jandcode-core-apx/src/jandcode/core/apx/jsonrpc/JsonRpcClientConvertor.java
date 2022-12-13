package jandcode.core.apx.jsonrpc;

import jandcode.core.dao.*;

/**
 * Конвертор результата выполнения dao для передачи клиенту
 */
public interface JsonRpcClientConvertor {

    /**
     * Конвертировать результат выполнения dao для передачи клиенту
     *
     * @param daoContext контекст исполнения dao
     * @param inst       результат выполнения dao
     * @return объект, которые будет передан клиенту
     */
    Object convert(DaoContext daoContext, Object inst);

}
