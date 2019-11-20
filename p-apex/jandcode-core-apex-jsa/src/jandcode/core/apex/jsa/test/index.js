/* Поддержка unit-тестирования
----------------------------------------------------------------------------- */

export * from 'jandcode.core.jsa.base/test'
import * as apex from 'jandcode.core.apex.jsa'
import {Vue} from 'jandcode.core.apex.jsa'

import styleCss from './style.css'

// css
Jc.requireCss(styleCss)

export let defaultPauseAfterEach = 250

Vue.config.errorHandler = function(err, vm, info) {
    console.error(`[Vue error]: ${err}${info}`)
    throw new Error(err)
}

Vue.config.warnHandler = function(err, vm, info) {
    console.error(`[Vue warn]: ${err}${info}`)
    throw new Error(err)
}

//////

/**
 * Возвращает элемент, куда можно выводить данные в тестах
 * @return {HTMLElement}
 */
export function getBody() {
    let id = "tst-wrapper--jc-app"
    let wrap = document.getElementById(id)
    if (!wrap) {
        wrap = document.createElement('div')
        wrap.id = id
        wrap.classList.add(id)
    }
    return wrap
}

export function cleanBody() {
    getBody().innerHTML = ''
}

export function hideBody() {
    getBody().classList.add('tst-hide')
}

export function showBody() {
    getBody().classList.remove('tst-hide')
}

export function initUi() {
    showBody()
    beforeEach(function() {
        cleanBody()
    });
}

export function pauseAfterEach(msec) {
    afterEach(function(cb) {
        setTimeout(function() {
            cb()
        }, msec || defaultPauseAfterEach)
    })
}

// скрываем место монтирования
hideBody()


/**
 * Создание и монтирование vue-компонента.
 * Возвращает экземпляр Vue
 * @param Comp компонент, может быть просто строкой-шаблоном
 * @param params параметры:
 * @param params.props свойства, которые будут переданы при рендеринге
 * @return {Vue|*}
 */
export function vueMount(Comp, params) {
    if (apex.isString(Comp)) {
        Comp = {
            template: Comp
        }
    }
    params = Object.assign({}, params)
    params.props = Object.assign({}, params.props)
    //
    let vm = new Vue({
        render(h) {
            return h(Comp, {props: params.props})
        }
    })
    vm.$mount()

    //
    let body = getBody()

    let el = document.createElement('div')
    el.classList.add('tst-vuecomp')
    el.appendChild(vm.$el)
    body.appendChild(el)

    return vm
}