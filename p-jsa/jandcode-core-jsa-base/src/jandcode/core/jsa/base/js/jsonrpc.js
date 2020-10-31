/* Json-Rpc
----------------------------------------------------------------------------- */

import * as base from './base'
import * as ajax from './ajax'
import * as error from './error'

/**
 * Простой клиент Json-Rpc
 */
export class JsonRpcClient {

    constructor(options) {

        /**
         * url, где находится обработчик
         * @type {string}
         */
        this.url = 'unknown'

        base.extend(this, options)
    }

    /**
     * Вызвать метод json rpc
     * @param methodName имя метода
     * @param methodParams параметры
     * @return {Promise}
     */
    invoke(methodName, ...methodParams) {
        let th = this
        let params = {}
        let id = base.nextId('json-rpc-')
        //
        if (Jc.cfg.envDev) {
            params._m = methodName.replace(/\//g, ':')
            console.info(`${id} ${th.url} start:`, methodName, 'params:', methodParams)
        }
        //
        let promise = new Promise(function(resolve, reject) {
            ajax.request({
                url: th.url,
                params: params,
                data: {
                    id: id,
                    method: methodName,
                    params: methodParams,
                }
            }).then((resp) => {
                if (Jc.cfg.envDev) {
                    console.info(`${id} ${th.url} ok   :`, methodName, 'result:', resp.data)
                }
                resolve(resp.data.result)
            }).catch((resp) => {
                if (Jc.cfg.envDev) {
                    console.info(`${id} ${th.url} ERROR:`, methodName, 'result:', resp.response.data)
                }
                reject(error.createError(resp.response.data.error))
            })
        });

        // маркируем
        promise.jsonRpcId = id

        return promise
    }
}