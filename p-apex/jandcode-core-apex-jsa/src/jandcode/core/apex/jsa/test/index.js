/* Поддержка unit-тестирования
----------------------------------------------------------------------------- */

export * from 'jandcode.core.jsa.base/test'

import styleCss from './style.css'

// css
Jc.requireCss(styleCss)

Vue.config.errorHandler = function(err, vm, info) {
    console.error(`[Vue error]: ${err}${info}`)
    throw new Error(err)
}

Vue.config.warnHandler = function(err, vm, info) {
    console.error(`[Vue warn]: ${err}${info}`)
    throw new Error(err)
}

export function cleanJcApp() {
    let wrap = document.getElementById("tst-wrapper--jc-app")
    wrap.innerHTML = '<div id="jc-app"></div>'
}

export function hideJcApp() {
    let wrap = document.getElementById("tst-wrapper--jc-app")
    wrap.classList.add('tst-hide')
}

export function showJcApp() {
    let wrap = document.getElementById("tst-wrapper--jc-app")
    wrap.classList.remove('tst-hide')
}

export function initUi() {
    showJcApp()
    beforeEach(function() {
        cleanJcApp()
    });
}

hideJcApp()
