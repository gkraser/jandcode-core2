/*

 ajax и все, что с ним связано

----------------------------------------------------------------------------- */

import * as base from './base'
import * as url from './url'
import {jQuery} from "./vendor"

/**
 * Простой ajax запрос. Обертка вогруг jQuery.ajax
 * @param config
 */
export function request(config) {
    let cfg = {};
    base.extend(cfg, config);
    //
    if (cfg.url) {
        cfg.url = url.ref(cfg.url);
    }
    if (cfg.params) {
        cfg.data = cfg.params;
        delete cfg.params;
    }

    //
    let xhr = jQuery.ajax(cfg);

    // оригинальный cfg сохраняем
    xhr.originalCfg = cfg;

    return xhr;
}

