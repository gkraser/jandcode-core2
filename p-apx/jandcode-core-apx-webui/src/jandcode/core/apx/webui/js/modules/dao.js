/* вызов dao
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

let _jsonRpcClients = {
    api: new jsaBase.jsonrpc.JsonRpcClient({url: 'api'})
}

/**
 * JsonRpc клиент для вызова dao в точке 'api'
 * @type {JsonRpcClient}
 */
export let daoApi = _jsonRpcClients.api


/**
 * JsonRpc клиент для вызова dao в точке url
 * @param url {String}
 * @return {JsonRpcClient}
 */
export function dao(url) {
    let client = _jsonRpcClients[url]
    if (!client) {
        client = new jsaBase.jsonrpc.JsonRpcClient({url: url})
        _jsonRpcClients[url] = client
    }
    return client
}