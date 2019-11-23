/*

jandcode-core-jsa-utils
 
----------------------------------------------------------------------------- */

import * as base from './base'
import * as dom from './dom'
import * as url from './url'
import * as popup from './popup'
import * as ajax from './ajax'
import * as moduleLoader from './moduleLoader'
import * as theme from './theme'
import cfg from './cfg'
import Jc from './globalNs'
import {jQuery} from './vendor'

export {
    dom,
    url,
    popup,
    ajax,
    theme,
    cfg,
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
Jc.applyTheme = theme.applyTheme
window.$ = jQuery
window.jQuery = jQuery
