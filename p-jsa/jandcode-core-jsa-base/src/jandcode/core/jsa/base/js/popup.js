/*

popup
Всплывающие объекты

Если используете, подключите jandcode/core/jsa/base/js/css/popup.css

----------------------------------------------------------------------------- */

import * as base from './base'
import * as cnv from './cnv'
import * as dom from './dom'

/**
 * Базовый z-index для контейнеров popup
 */
let zIndexBase = 10000

/**
 * dataset атрибуты для пометки и поиска popupEl и ownerEl
 */
let MARK_POPUP_EL = 'popupMarkerPopupEl'
let MARK_POPUP_EL_SELECTOR = '[data-popup-marker-popup-el]'
let MARK_OWNER_EL = 'popupMarkerOwnerEl'
let MARK_OWNER_EL_SELECTOR = '[data-popup-marker-owner-el]'

/**
 * Типы выравнивания
 */
export let alignPopupTypes = {
    dropdown: alignPopup_dropdown,
    submenu: alignPopup_submenu,
    contextmenu: alignPopup_contextmenu,
    center: alignPopup_center,
}

////// align funcs

/**
 * Интерфейс функций alignPopup_XXX.
 *
 * @param nodeOwn узел dom, относительно какого выравниваем
 * @param nodePop узел dom, что выравниваем
 * @param own границы узла nodeOwn
 * @param pop границы узла nodePop. В этот объект необходимо записать
 *            новые координаты x,y и при необходимости размер w,h
 * @param vp размеры экрана
 * @param params параметры выравнивания
 */
function alignPopup_INTERFACE(nodeOwn, nodePop, own, pop, vp, params) {
}

/**
 * Выровнять под nodeOwn. Для dropdown-стиля. Если не помещяется - осуществляется
 * подбор.
 */
function alignPopup_dropdown(nodeOwn, nodePop, own, pop, vp, params) {
    let ok = false

    if (pop.w < own.w) {
        pop.w = own.w;
    }

    // горизонтально
    pop.x = own.x;
    if (own.x + pop.w > vp.w) {
        // не помещается, пробуем сдвинуть влево до границы own
        if (own.x + own.w - pop.w > 0) {
            // так помещается
            pop.x = own.x + own.w - pop.w;
        } else {
            // вообще не помещается
            pop.x = 0;
        }
    }

    // вертикально
    if (own.y + own.h + pop.h > vp.h) {
        // не помещается внизу,пробуем переместить вверх
        if (own.y - pop.h > 0) {
            // помещается вверху
            pop.y = own.y - pop.h;
            ok = true;
        }
    } else {
        // полностью помещается внизу
        pop.y = own.y + own.h;
        ok = true;
    }
    if (!ok) {
        // не помещается, уменьшаем высоту
        // где больше места: вверху или внизу?
        let topH = own.y
        let botH = vp.h - own.y - own.h
        if (topH > botH) {
            // вверху больше
            if (topH < pop.h) {
                pop.h = topH;
            }
            pop.y = own.y - pop.h;
        } else {
            // внизу больше
            if (botH < pop.h) {
                pop.h = botH;
            }
            pop.y = own.y + own.h;
        }
    }
}

/**
 * Выравнивание справа nodeOwn.
 * Для submenu-стиля. Если не помещяется - осуществляется подбор.
 */
function alignPopup_submenu(nodeOwn, nodePop, own, pop, vp, params) {
    let ok = false;
    pop.y = own.y;
    if (own.x + own.w + pop.w <= vp.w) {
        // помещается справа
        pop.x = own.x + own.w;
        ok = true;
    } else {
        // не помещается справа, пробуем слева
        if (own.x - pop.w >= 0) {
            // слева помещается
            pop.x = own.x - pop.w;
            ok = true;
        }
    }
    if (!ok) {
        // не помещается ни справа, ни слева
        // сдвигаем
        pop.x = vp.w - pop.w;
    }
    // теперь проверяем высоту, что бы не вылазило
    if (pop.y + pop.h > vp.h) {
        // вылазит
        pop.y = vp.h - pop.h;
        if (pop.y < 0) {
            pop.y = 0;
        }
    }
}

/**
 * Выравнивание справа nodeOwn.
 * Для submenu-стиля. Если не помещяется - осуществляется подбор.
 */
function alignPopup_contextmenu(nodeOwn, nodePop, own, pop, vp, params) {
    let mousePos = dom.getCurrentMousePos()
    pop.x = params.x == null ? mousePos.x : params.x;
    pop.y = params.y == null ? mousePos.y : params.y;

    if (pop.x + pop.w > vp.w) {
        // вылазиет справа, сдвигаем
        pop.x = vp.w - pop.w;
        if (pop.x < 0) {
            pop.x = 0;
        }
    }
    if (pop.y + pop.h > vp.h) {
        // вылазиет снизу, сдвигаем
        pop.y = vp.h - pop.h;
        if (pop.y < 0) {
            pop.y = 0;
        }
    }
}

/**
 * Выравнивание по центру экрана.
 * Для диалоговых окон. Если не помещяется - осуществляется подбор.
 */
function alignPopup_center(nodeOwn, nodePop, own, pop, vp, params) {

    // отступы
    let sp_w = 50
    let sp_h = 50

    if (pop.w + sp_w > vp.w) {
        pop.w = vp.w - sp_w
    }
    if (pop.h + sp_h > vp.h) {
        pop.h = vp.h - sp_h
    }

    pop.x = Math.round((vp.w - pop.w) / 2)
    pop.y = Math.round((vp.h - pop.h) / 2)
}


/**
 * Выровнять узел относительно другого. С магией.
 * @param nodeOwn относительно какого выравниваем
 * @param nodePop что выравнимаем
 * @param params {Object|String} параметры выравнивания.
 *        Если строка, преобразуется в объект {type:VALUE}
 * @param params.type {String} тип выравнивания. Имя функции в объекте
 *        alignPopupTypes
 */
export function alignPopup(nodeOwn, nodePop, params) {
    let pms = {}
    if (cnv.isObject(params)) {
        pms = Object.assign(pms, params)
    } else if (cnv.isString(pms)) {
        pms = {type: pms};
    }

    // выбираем алгоритм выравнивания
    let fn = alignPopupTypes[pms.type]
    if (!fn) {
        fn = alignPopupTypes.dropdown;
    }

    // собираем координаты

    // fix: если action на toolbar и перед ней есть flex-grow и нет scroll на странице,
    // то координаты action после присвоения координат для popup меняются,
    // как бы учитывается не существующая scrollbar
    // этот фикс вроде как обходит эту проблему
    dom.setNodeBound(nodePop, {x: 0, y: 0});
    // end fix

    let own = nodeOwn ? dom.getNodeBound(nodeOwn) : {x: 0, y: 0, w: 0, h: 0}
    let pop = dom.getNodeBound(nodePop)
    let vp = dom.getViewportBound()

    // запоминаем оригинальный размер pop
    pop._w = pop.w;
    pop._h = pop.h;

    // выполняем выравнивание
    fn(nodeOwn, nodePop, own, pop, vp, pms);

    // выявляем необходимость смены размера
    if (pop.w === pop._w) {
        delete pop.w;
    }
    if (pop.h === pop._h) {
        delete pop.h;
    }

    // устанавливаем границы
    dom.setNodeBound(nodePop, pop);
}

/**
 * Объект popup.
 * Экземпляры этого класса используются как popup.
 */
class Popup {

    constructor() {

        /**
         * Параметры для показа popup. Зависят от способа выравнивания.
         * @return {Object}
         */
        this.params = {}

        /**
         * Элемент-контейнер, который будет использован как popup.
         * @return {Element}
         */
        this.popupEl = null

        /**
         * Элемент-владелец для popup, относительно которого он будет показан.
         * Может отсутсвовать. Например для contextmenu не нужен.
         * @return {Element}
         */
        this.ownerEl = null

        /**
         * Признак модального popup
         * @type {boolean}
         */
        this.modal = false

        /**
         * Метод (если есть) вызывается после показа popup
         */
        this.onShow = null

        /**
         * Метод (если есть) вызывается после закрытия popup
         */
        this.onClose = null

    }

    getParams() {
        return this.params
    }

    getPopupEl() {
        return this.popupEl
    }

    getOwnerEl() {
        return this.ownerEl
    }

}

/**
 * Менеджер popup
 */
export class PopupManager {

    constructor() {
        // стек объектов IPopup
        // все, что в этом стеке - показано сейчас на экране
        this.stack = []
        //
        let th = this
        base.ready(function() {
            document.body.addEventListener("mousedown", th.onMouseDown.bind(th))
            document.body.addEventListener("keydown", th.onKeydown.bind(th))
        })

        // маска для диалогов
        this.maskEl = document.createElement("div")
        this.maskEl.classList.add("jc-popup-mask")
        this.maskEl.style['z-index'] = zIndexBase;

    }

    /**
     * Создать новый экземпляр popup
     * @return {Popup}
     */
    createPopup() {
        return new Popup()
    }

    /**
     * Показан ли этот popup
     * @param popup {IPopup}
     */
    isShowed(popup) {
        return this.stack.indexOf(popup) !== -1
    }

    /**
     * Найти popup по узлу, ему принадлежащему
     * @param node {Element}
     */
    findByPopupEl(node) {
        for (let popup of this.stack) {
            if (popup.getPopupEl() === node) {
                return popup
            }
        }
        return null
    }

    /**
     * Найти popup по узлу ownerEl, ему принадлежащему
     * @param node {Element}
     */
    findByOwnerEl(node) {
        for (let popup of this.stack) {
            if (popup.getOwnerEl() === node) {
                return popup
            }
        }
        return null
    }

    /**
     * Замаскировать модальный popup
     */
    maskModal() {
        let pModal = null
        for (let i = this.stack.length - 1; i >= 0; i--) {
            let pit = this.stack[i]
            if (pit.modal) {
                pModal = pit
                break
            }
        }
        if (!pModal) {
            // нет модальных
            this.maskEl.remove()
        } else {
            // вставляем перед верхним модальным
            document.body.insertBefore(this.maskEl, pModal.popupEl)
        }
    }

    /**
     * Показать popup
     * @param popup {IPopup} что показать
     */
    showPopup(popup) {
        if (!popup || this.isShowed(popup)) {
            return;
        }
        this.stack.push(popup);

        let popupEl = popup.getPopupEl()
        popupEl.dataset[MARK_POPUP_EL] = "1"
        popupEl.style['position'] = 'absolute';
        popupEl.style['z-index'] = zIndexBase;

        let ownerEl = popup.getOwnerEl();
        if (ownerEl) {
            ownerEl.dataset[MARK_OWNER_EL] = '1'
        }

        document.body.appendChild(popupEl);

        alignPopup(ownerEl, popupEl, popup.getParams());

        this.maskModal()

        //
        if (cnv.isFunction(popup.onShow)) {
            popup.onShow()
        }
    }

    /**
     * Есть ли показанные popup
     */
    hasPopup() {
        return this.stack.length > 0;
    }

    /**
     * Закрывает popup и все его вышестоящие.
     * @param popup {IPopup}
     */
    closePopup(popup) {
        let idx = this.stack.indexOf(popup)
        if (idx === -1) {
            return;
        }
        for (let i = this.stack.length - 1; i >= idx; i--) {
            let pit = this.stack[i]
            this.doClosePopup(pit);
            this.stack.pop();
        }
        this.maskModal()
    }

    /**
     * Закрывает все popup
     */
    closePopupAll() {
        if (!this.hasPopup()) {
            return;
        }
        this.closePopup(this.stack[0]);
    }

    /**
     * Закрывает все popup выше последнего модального
     */
    closePopupAllAboveModal() {
        if (!this.hasPopup()) {
            return;
        }
        let p = null
        for (let i = this.stack.length - 1; i >= 0; i--) {
            let pit = this.stack[i]
            if (pit.modal) {
                break
            }
            p = pit
        }
        if (p) {
            this.closePopup(p);
        }
    }

    /**
     * Закрывает все popup выше указанного
     * @param popup {IPopup}
     */
    closePopupUp(popup) {
        if (!this.hasPopup()) {
            return;
        }
        let idx = this.stack.indexOf(popup)
        if (idx === -1 || idx === this.stack.length - 1) {
            return;
        }
        this.closePopup(this.stack[idx + 1]);

    }

    /**
     * Закрывает последний (самый верхний) popup
     */
    closePopupLast() {
        if (!this.hasPopup()) {
            return;
        }
        this.closePopup(this.stack[this.stack.length - 1])
    }

    /**
     * Процесс закрытия конкретного popup
     * @param popup {IPopup}
     */
    doClosePopup(popup) {
        let popupEl = popup.getPopupEl()
        document.body.removeChild(popupEl);
        delete popupEl.dataset[MARK_POPUP_EL]

        let ownerEl = popup.getOwnerEl();
        if (ownerEl) {
            delete ownerEl.dataset[MARK_OWNER_EL]
        }

        if (cnv.isFunction(popup.onClose)) {
            popup.onClose()
        }
    }

    onMouseDown(ev) {

        let el, popup;

        el = ev.target.closest(MARK_OWNER_EL_SELECTOR)
        if (el) {
            popup = this.findByOwnerEl(el);
            if (popup) {
                this.closePopupUp(popup)
                return;
            }
        }

        el = ev.target.closest(MARK_POPUP_EL_SELECTOR)
        if (el) {
            popup = this.findByPopupEl(el);
            if (popup) {
                this.closePopupUp(popup)
                return;
            }
        }

        // ткнули мимо любого popup

        if (this.hasModal()) {
            ev.preventDefault()
            ev.stopPropagation()
            this.closePopupAllAboveModal()
        } else {
            this.closePopupAll();
        }
    }

    onKeydown(ev) {
        if (ev.code === 'Escape') {
            this.closePopupLast();
        }
    }

    hasModal() {
        for (let popup of this.stack) {
            if (popup.modal) {
                return true
            }
        }
        return false
    }

}

/**
 * Менеджер popup по умолчанию
 * @type {PopupManager}
 */
export let popupManager = new PopupManager()
