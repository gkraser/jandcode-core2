/* вызов dao
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

/**
 * JsonRpc клиент для вызова dao в точке 'api'
 * @type {JsonRpcClient}
 */
export let daoApi = new jsaBase.jsonrpc.JsonRpcClient({url: 'api'})


