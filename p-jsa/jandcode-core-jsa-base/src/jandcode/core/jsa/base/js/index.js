/*

jandcode-core-jsa-base
 
----------------------------------------------------------------------------- */

import * as base from './base'
import * as dom from './dom'
import * as url from './url'
import * as popup from './popup'
import * as moduleLoader from './moduleLoader'
import * as theme from './theme'
import * as ajax from './ajax'
import * as svgicons from './svgicons'
import * as jsonrpc from './jsonrpc'
import cfg from './cfg'
import Jc from './globalNs'
import {jQuery, axios} from './vendor'

export {
    dom,
    url,
    popup,
    theme,
    ajax,
    svgicons,
    jsonrpc,
    cfg,
    jQuery,
    axios,
}

// модули на верхний уровень
export * from './base'
export * from './cnv'
export * from './moduleLoader'
export * from './error'
export * from './app'
export * from './wait'

// глобализация
Jc.ready = base.ready;
Jc.loadModule = moduleLoader.loadModule
Jc.applyTheme = theme.applyTheme
window.$ = jQuery
window.jQuery = jQuery
