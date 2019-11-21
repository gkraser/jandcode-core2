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
 * @param params.propsData свойства, которые будут переданы при рендеринге
 * @return {Vue|*} отрендеренный компонент
 */
export function vueMount(Comp, params) {
    if (apex.isString(Comp)) {
        Comp = {
            template: Comp
        }
    }
    params = Object.assign({}, params)

    let CompMixin = {
        methods: {
            async setPropsData(props) {
                for (let pn in props) {
                    this.$set(this.$parent.propsData, pn, props[pn])
                }
                await this.$nextTick()
            }
        }
    }

    let Comp1 = {
        mixins: [CompMixin, Comp]
    }

    //
    let vm = new Vue({
        data() {
            return {
                propsData: Object.assign({}, params.propsData),
            }
        },
        render(h) {
            return h(Comp1, {props: this.propsData})
        }
    })
    vm.$mount()

    //
    let body = getBody()

    let el = document.createElement('div')
    el.classList.add('tst-vuecomp')
    el.appendChild(vm.$el)
    body.appendChild(el)

    return vm.$children[0]
}