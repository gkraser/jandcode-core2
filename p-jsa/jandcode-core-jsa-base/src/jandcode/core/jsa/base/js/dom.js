/*

Утилиты для dom/html

----------------------------------------------------------------------------- */

import * as base from './base'
import * as cnv from './cnv'
import lodashDebounce from 'lodash/debounce'

/**
 * @typedef {Object} DomPoint
 * @property {number} x x
 * @property {number} y y
 */

/**
 * @typedef {Object} DomRect
 * @property {number} x x
 * @property {number} y y
 * @property {number} w width
 * @property {number} h height
 */

// текущая позиция мыши
let _currentMousePos = {
    x: 0,
    y: 0
}

/**
 * Текущая позиция мыши {x,y} относительно окна.
 * @return {DomPoint}
 */
export function getCurrentMousePos() {
    return Object.assign({}, _currentMousePos)
}


/**
 * Конвертация значения a в размер для стиля:
 * 12 -> '12px'
 * '12' -> '12px'
 * 'any' -> 'any'
 * @param a
 * @return {String}
 */
export function toStyleSize(a) {
    if (a == null || a === '') return '';
    if (cnv.isNumber(a)) {
        return '' + a + 'px';
    }
    if (cnv.isNumChar(a.charAt(a.length - 1))) {
        return a + 'px';
    }
    return '' + a;
}

/**
 * Возвращает прямоугольник для элемента (x,y,w,h)
 * относительно экрана
 * @param node {Element}
 * @return {DomRect}
 */
export function getNodeBound(node) {
    let a = node.getBoundingClientRect();
    return {
        x: a.left,
        y: a.top,
        w: a.width,
        h: a.height
    }
}

/**
 * Устанавливает позицию и размер элемента (x,y,w,h)
 * относительно экрана.
 *
 * @param node {Element}
 * @param b {DomRect}
 */
export function setNodeBound(node, b) {
    if (!b) {
        return;
    }
    if ('x' in b) {
        node.style.left = toStyleSize(b.x + window.pageXOffset);
    }
    if ('y' in b) {
        node.style.top = toStyleSize(b.y + window.pageYOffset);
    }
    if ('w' in b) {
        node.style.width = toStyleSize(b.w);
    }
    if ('h' in b) {
        node.style.height = toStyleSize(b.h);
    }
}

/**
 * Размер экрана
 * @return {DomRect}
 */
export function getViewportBound() {
    return {
        x: 0,
        y: 0,
        w: window.innerWidth,
        h: window.innerHeight
    }
}

let __createTmpElement_place = null

/**
 * Создает элемент, который лежит в dom, видим, но за пределами экрана.
 * После использования его нужно удалять, что бы не замусоривать dom.
 * Используется для отложенного рендеринга вещей, которые правильно рендярятся
 * только в реальные видимые элементы.
 *
 * @param tagName имя тега (по умолчанию 'div')
 * @param id id тега, если установлен
 */
export function createTmpElement(tagName, id) {
    if (__createTmpElement_place == null) {
        __createTmpElement_place = document.createElement('div')
        __createTmpElement_place.id = base.nextId('tmp-place-')
        __createTmpElement_place.style.position = 'fixed'
        __createTmpElement_place.style.zIndex = '100500'
        __createTmpElement_place.style.maxWidth = '100vw'
        __createTmpElement_place.style.left = '500vw'
        __createTmpElement_place.style.top = '0'
        document.body.appendChild(__createTmpElement_place)
    }
    let el = document.createElement(tagName || 'div')
    el.id = id || base.nextId('tmp-node-')
    __createTmpElement_place.appendChild(el)
    return el
}

//////

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

//////

/**
 * Инициализация модуля
 * @private
 */
function _init() {

    // отслеживание текущей позиции мыши
    window.addEventListener("mousemove", function(ev) {
        _currentMousePos.x = ev.clientX;
        _currentMousePos.y = ev.clientY;
    });

}

//////
base.ready(function() {
    _init()
})

