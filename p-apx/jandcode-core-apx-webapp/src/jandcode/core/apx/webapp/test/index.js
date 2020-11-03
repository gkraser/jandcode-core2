/* Поддержка unit-тестирования
----------------------------------------------------------------------------- */

export * from 'jandcode.core.jsa.base/test'
import {Vue, jsaBase} from '../index'

import styleCss from './style.css'

// css
Jc.requireCss(styleCss)

/**
 * Пауза по умолчанию
 * @type {number}
 */
export let defaultPause = 250

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
 * Инициализация среды тестирования
 */
function init() {
    before(function() {
        initEnv()
    })

    beforeEach(function() {
        cleanBody()
        showBody()
    });
}

function initEnv() {
    let mochaEl = document.getElementById("mocha")
    if (!mochaEl) {
        mochaEl = document.createElement('div')
        mochaEl.id = 'mocha'
    }
    //
    let html = `<div id="tst-wrapper" class="tst-wrapper">
  <div id="tst-wrapper--mocha" class="tst-wrapper--mocha">
    <div id="tmp-place-mocha"></div>
  </div>

  <div id="tst-wrapper--jc-app" class="tst-wrapper--jc-app">
  </div>
</div>
`
    document.body.insertAdjacentHTML('afterbegin', html)
    //
    let tmp_mochaEl = document.getElementById("tmp-place-mocha")
    tmp_mochaEl.parentElement.replaceChild(mochaEl, tmp_mochaEl)
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

export function pauseAfterEach(msec) {
    afterEach(function(cb) {
        setTimeout(function() {
            cb()
        }, msec || defaultPause)
    })
}

export function pause(msec) {
    msec = msec || defaultPause
    return new Promise(function(resolve) {
        setTimeout(function() {
            resolve()
        }, msec)
    })
}

/**
 * Создание и монтирование vue-компонента.
 * Возвращает экземпляр Vue
 * @param Comp компонент, может быть просто строкой-шаблоном
 * @param params параметры:
 * @param params.propsData свойства, которые будут переданы при рендеринге
 * @return {Vue|*} отрендеренный компонент
 */
export function vueMount(Comp, params) {
    if (jsaBase.isString(Comp)) {
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

    // функциональный ли?
    let functional = Comp.functional

    //
    let vm = new Vue({
        data() {
            return {
                propsData: Object.assign({}, params.propsData),
            }
        },
        render(h) {
            let z = h(Comp1, {props: this.propsData})
            if (functional) {
                return h('div', [
                    z
                ])
            } else {
                return z
            }
        }
    })
    vm.$mount()

    //
    let body = getBody()

    if (body.childElementCount > 0) {
        let el = document.createElement('div')
        el.classList.add('tst-vuecomp-separator')
        body.appendChild(el)
    }

    let el = document.createElement('div')
    el.classList.add('tst-vuecomp')
    el.appendChild(vm.$el)
    body.appendChild(el)

    return vm.$children[0]
}

//////

init()
