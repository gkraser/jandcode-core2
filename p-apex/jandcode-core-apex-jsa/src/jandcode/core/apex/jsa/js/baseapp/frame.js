/* Фреймы
----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import Dialog from './dialog/Dialog'

// настроки темы по умолчанию
jsaBase.cfg.setDefault({
    theme: {
        dialog: {
            flatButtons: false
        }
    }
})

/**
 * Настройки кнопок для диалогов
 */
export let dialogButtons = {
    'ok': {label: 'Ок', cm: 'ok', color: 'positive'},
    'cancel': {label: 'Отмена', cm: 'cancel', color: 'negative'},
    'close': {label: 'Закрыть', cm: 'cancel', color: 'secondary'},
    'yes': {label: 'Да', cm: 'yes', color: 'positive'},
    'no': {label: 'Нет', cm: 'no', color: 'secondary'},
    'save': {label: 'Сохранить', cm: 'ok', color: 'positive'},
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

function showDialog(frameComp, params) {
    params = jsaBase.extend({}, params)
    let props = jsaBase.extend({}, params.props)

    let FrameCompCls = Vue.extend(frameComp)
    let frameInst = new FrameCompCls({propsData: props})
    frameInst.$mount()

    let DialogCls = Vue.extend(Dialog)
    let dialogInst = new DialogCls({propsData: {frameInst: frameInst}})
    dialogInst.$on('dialog-close', function() {
        console.info("hook dialog-close", arguments);
        frameInst.$destroy()
        dialogInst.$destroy()
    })
    dialogInst.$mount()

    dialogInst.showDialog()

    console.info("frameInst", frameInst);
}


export {
    showDialog,
}
