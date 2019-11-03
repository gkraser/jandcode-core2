/*

jandcode-core-jsa-utils
 
----------------------------------------------------------------------------- */

import * as base from './base'
import * as cnv from './cnv'
import * as dom from './dom'
import * as url from './url'
import * as popup from './popup'
import {popupManager} from './popup'
import * as ajax from './ajax'
import * as moduleLoader from './moduleLoader'
import * as error from './error'
import * as funcs from './funcs'
import cfg from './cfg'
import Jc from './globalNs'
import {jQuery} from './vendor'

export {
    base,
    cnv,
    dom,
    url,
    popup,
    popupManager,
    ajax,
    error,
    funcs,
    cfg,
    Jc,
    jQuery,
}

// модули на верхний уровень
export * from './base'
export * from './cnv'
export * from './moduleLoader'
export * from './error'

// глобализация
Jc.ready = base.ready;
Jc.loadModule = moduleLoader.loadModule
Jc.errorShow = error.errorShow
window.$ = jQuery
window.jQuery = jQuery
