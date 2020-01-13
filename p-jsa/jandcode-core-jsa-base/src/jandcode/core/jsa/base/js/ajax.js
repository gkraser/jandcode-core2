/* ajax, основан на axios
----------------------------------------------------------------------------- */

import {axios} from './vendor'
import * as url from './url'

/**
 * axios.request с некоторыми умолчаниями, настроенными на среду jandcode
 * @param config {Object}
 */
export function request(config) {
    config = Object.assign({}, config)
    config.headers = Object.assign({}, config.headers)

    //
    if (config.url) {
        config.url = url.ref(config.url)
    }

    //
    if (!config.method) {
        config.method = 'post'
    }

    // маркер ajax запроса
    config.headers['X-Requested-With'] = 'XMLHttpRequest';

    //
    return axios.request(config)
}