/* ajax, основан на axios
----------------------------------------------------------------------------- */

import {axios} from './vendor'
import * as url from './url'
import {waitShow, waitHide} from './wait'

/**
 * axios.request с некоторыми умолчаниями, настроенными на среду jandcode
 *
 * url оборачивается функцией url.ref()
 *
 * formdata=true в этом случае данные передаются в формате
 * application/x-www-form-urlencoded: обавляется заголовок
 * application/x-www-form-urlencoded и data конвертируется в строку параметров
 * и помещается в data
 *
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

    if (config.formdata) {
        config.headers['Content-Type'] = 'application/x-www-form-urlencoded'
        config.data = url.params(config.data)
    }

    // маркер ajax запроса
    config.headers['X-Requested-With'] = 'XMLHttpRequest'

    //
    return axios.request(config)
}

// wait show

function ajax_waitShow(config) {
    if (config.waitShow === false) {
        return
    }
    config.__waitShowed = true
    waitShow()
}

function ajax_waitHide(config) {
    if (config.__waitShowed) {
        delete config.__waitShowed
        waitHide()
    }
}

axios.interceptors.request.use(function(config) {
    ajax_waitShow(config)
    return config;
}, function(error) {
    ajax_waitHide(error.config)
    return Promise.reject(error);
})

axios.interceptors.response.use(function(response) {
    ajax_waitHide(response.config)
    return response;
}, function(error) {
    ajax_waitHide(error.config)
    return Promise.reject(error);
})
