import lodashDebounce from 'lodash/debounce'

class ResizeWatcher {

    constructor(el, trigger, debonceTimeout) {
        if (!el) {
            throw new Error("el not defined")
        }
        if (!trigger) {
            throw new Error("trigger not defined")
        }
        if (debonceTimeout == null) {
            debonceTimeout = 100
        }
        this.el = el
        this.listener = (ev) => {
            trigger(ev)
        }
        this.listener_dobonced = lodashDebounce(this.listener, debonceTimeout)
        this.rso = new ResizeObserver(this.listener_dobonced)
        this.rso.observe(this.el)
    }

    destroy() {
        this.rso.disconnect()
        this.listener_dobonced.cancel()
        this.listener_dobonced = null
        this.el = null
        this.rso = null
        this.listener = null
    }

}

/**
 * Установить наблюдение за измененем размеров dom-элемента
 * @param el за каким элементом наюлюдаем
 * @param trigger функция будет вызвана, когда размер изменится
 * @param debonceTimeout пауза между многократными вызовами, что бы уменьшить число
 * вызовов trigger. По умолчанию - 100
 * @return {ResizeWatcher} возвращает объект с методом destroy(), который нужно вызвать
 * что бы отменить наблюдение
 */
export function resizeWatch(el, trigger, debonceTimeout) {
    return new ResizeWatcher(el, trigger, debonceTimeout)
}

