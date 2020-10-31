/* вызов dao
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'


/**
 * Вызов dao на сервере. Вызывается чераз action 'api'
 * @param daoMethod имя метода
 * @param daoParams параметры
 * @return {Promise<unknown>}
 */
export function apiCall(daoMethod, ...daoParams) {
    let url = 'api'
    let params = {}
    let id = jsaBase.nextId('dao-')
    //
    if (Jc.cfg.envDev) {
        params._m = daoMethod.replace(/\//g, ':')
        console.info(`apiCall ${id} start:`, daoMethod, 'params:', daoParams)
    }
    //
    return new Promise(function(resolve, reject) {
        jsaBase.ajax.request({
            url: url,
            params: params,
            data: {
                id: id,
                method: daoMethod,
                params: daoParams,
            }
        }).then((resp) => {
            if (Jc.cfg.envDev) {
                console.info(`apiCall ${id} ok   :`, daoMethod, 'result:', resp.data)
            }
            resolve(resp.data.result)
        }).catch((resp) => {
            if (Jc.cfg.envDev) {
                console.info(`apiCall ${id} ERROR:`, daoMethod, 'result:', resp.response.data)
            }
            reject(jsaBase.createError(resp.response.data.error))
        })
    });

}
