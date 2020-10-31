/* вызов dao
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

/**
 * Вызов dao на сервере
 * @param daoMethod имя метода
 * @param daoParams параметры
 * @return {Promise<unknown>}
 */
export function invokeDao(daoMethod, ...daoParams) {
    let url = 'dao/invokeDao'
    let params = {}
    if (Jc.cfg.envDev) {
        params._m = daoMethod.replace(/\//g, ':')
    }
    let id = jsaBase.nextId('dao-')
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
            resolve(resp.data.result)
        }).catch((resp) => {
            reject(jsaBase.createError(resp.response.data.error))
        })
    });

}
