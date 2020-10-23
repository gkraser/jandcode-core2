/* Фреймы
----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import Dialog from './dialog/Dialog'
import upperFirst from 'lodash/upperFirst'

import {ComponentHolder} from './component-holder'

// опция initFrame будет выглядеть как массив (аналогично другим life-cycle hookd)
Vue.config.optionMergeStrategies.initFrame = Vue.config.optionMergeStrategies.created

// настройки темы по умолчанию
jsaBase.cfg.setDefault({
    theme: {
        dialog: {
            flatButtons: false
        },
        componentHolder: {}
    }
})

/**
 * Хранилище компонентов
 * @type {ComponentHolder}
 */
export let componentHolder = new ComponentHolder()

/**
 * Настройки кнопок для диалогов
 */
export let dialogButtons = {
    'ok': {label: 'Ок', cmd: 'ok', color: 'positive'},
    'cancel': {label: 'Отмена', cmd: 'cancel', color: 'negative'},
    'close': {label: 'Закрыть', cmd: 'cancel', color: 'secondary'},
    'yes': {label: 'Да', cmd: 'yes', color: 'positive'},
    'no': {label: 'Нет', cmd: 'no', color: 'secondary'},
    'save': {label: 'Сохранить', cmd: 'ok', color: 'positive'},
}


export let dialogButtonGroups = {
    'ok': ['ok'],
    'ok-cancel': ['ok', 'cancel'],
    'close': ['close'],
    'yn': ['yes', 'no'],
    'ync': ['yes', 'no', 'cancel'],
    'save': ['save', 'cancel'],
}

/**
 * Получение списка определения кнопок для диалога
 * @param buttons либо имя из dialogButtonGroups, либо массив имен кнопок из
 * dialogButtons, либо массив определений кнопок
 */
export function getDialogButtons(buttons) {
    if (!buttons) {
        buttons = 'ok'
    }
    let bArray = buttons
    if (jsaBase.isString(bArray)) {
        bArray = dialogButtonGroups[buttons]
        if (!bArray) {
            bArray = dialogButtonGroups['ok']
        }
    }
    let res = []
    for (let b of bArray) {
        if (jsaBase.isString(b)) {
            b = dialogButtons[b]
            if (!b) {
                b = dialogButtons['close']
            }
        }
        let b1 = jsaBase.extend({}, b)
        res.push(b1)
    }
    return res
}

export class Shower {

    constructor(params) {
        // копия параметров
        this.params = jsaBase.extend({}, params)

        // свойства для фрейма
        this.propsData = jsaBase.extend({}, params.propsData)

        // компонент фрейма
        this.frame = this.params.frame

        // экземпляр фрейма
        this.frameInst = null
    }

    showFrame() {
    }

    /**
     * Закрыть фрейм с указанной командой
     * @param cmd
     */
    closeFrame(cmd) {
    }

    /**
     * Инициализация фрейма.
     * Вызов initFrame
     */
    async initFrame(callback) {
        let initFrameArr = this.frameInst.$options['initFrame']
        if (initFrameArr) {
            for (let fn of initFrameArr) {
                await fn.call(this.frameInst)
            }
        }
        callback()
    }

}

export class ShowerDialog extends Shower {

    showFrame() {
        let th = this

        let FrameCompCls = Vue.extend(th.frame)
        th.frameInst = new FrameCompCls({propsData: th.propsData})
        th.frameInst.shower = th

        th.initFrame(() => {
            th.frameInst.$mount()
            th.dialogEl = jsaBase.dom.createTmpElement()

            let DialogCls = Vue.extend(Dialog)
            th.dialogInst = new DialogCls({propsData: {frameInst: th.frameInst}})
            th.dialogInst.$on('dialog-close', function() {
                th.frameInst.$destroy()
                th.frameInst.shower = null
                th.dialogInst.$destroy()
                th.dialogEl.remove()
                th.frameInst = null
                th.dialogInst = null
                th.dialogEl = null
            })
            th.dialogInst.$mount(th.dialogEl)

            th.dialogInst.showDialog()
        })

        return th.frameInst
    }

    closeFrame(cmd) {
        // сначала события самого фпейма
        this.__closeFrameProcess(cmd, this.frameInst, () => {
            // фрейм разрешил закрытся
            this.__closeFrameProcess(cmd, this.params, () => {
                // обработчики в параметрах разрешили закрытся
                this.dialogInst.hideDialog()
            })
        })
    }

    __closeFrameProcess(cmd, eventsOwner, fnClose) {
        if (!cmd) {
            cmd = 'cancel'
        }
        let handlerName = 'on' + upperFirst(cmd)

        let handleProcess = (handlerResult) => {
            if (handlerResult === false) {
                // закрывать нельзя
            } else if (handlerResult instanceof Promise) {
                // ждем окончания promise
                handlerResult.then((result) => {
                    if (result === false) {
                        // promise вернул false, закрывать нельзя
                        return
                    }
                    fnClose()
                })
            } else {
                // можно закрывать
                fnClose()
            }
        }

        if (jsaBase.isFunction(eventsOwner[handlerName])) {
            // есть обработчик onXxx
            handleProcess(eventsOwner[handlerName](this.frameInst, cmd))

        } else if (jsaBase.isFunction(eventsOwner.onCmd)) {
            handleProcess(eventsOwner.onCmd(this.frameInst, cmd))

        } else {
            // нет обработчиков
            fnClose()
        }
    }

}

export function showDialog(params) {
    let shower = new ShowerDialog(params)
    return shower.showFrame()
}

