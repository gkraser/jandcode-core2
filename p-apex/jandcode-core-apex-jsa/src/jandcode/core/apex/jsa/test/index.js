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
            setProps(props) {
                if (!props) {
                    return
                }
                if (apex.isObject(props)) {
                    let dest = this.$parent.rootProps
                    for (let pn in props) {
                        let v = props[pn]
                        this.$set(dest, pn, v)
                    }
                    this.$parent.$forceUpdate()
                    this.$forceUpdate()
                    console.info("updated");
                }
            }
        }
    }

    let Comp1 = {
        mixins: [CompMixin, Comp]
    }

    //
    let vm = new Vue({
        data() {
            let props = Object.assign({}, params.props)
            return {
                rootProps: props,
            }
        },
        render(h) {
            return h(Comp1, {props: this.rootProps})
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