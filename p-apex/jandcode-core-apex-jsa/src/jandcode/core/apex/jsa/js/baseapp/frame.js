/* Фреймы
----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import Dialog from './dialog/Dialog'

// настройки темы по умолчанию
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

class Shower {

    constructor(params) {
        // копия параметров
        this.params = jsaBase.extend({}, params)

        // свойства для фрейма
        this.props = jsaBase.extend({}, params.props)

        // компонент фрейма
        this.frame = this.params.frame
    }

    showFrame() {
    }

    /**
     * Закрыть фрейм с указанной командой
     * @param cmd
     */
    closeFrame(cmd) {
    }
}

class ShowerDialog extends Shower {

    showFrame() {
        let th = this

        let FrameCompCls = Vue.extend(th.frame)
        th.frameInst = new FrameCompCls({propsData: th.props})
        th.frameInst.shower = th
        th.frameInst.$mount()

        let DialogCls = Vue.extend(Dialog)
        th.dialogInst = new DialogCls({propsData: {frameInst: th.frameInst}})
        th.dialogInst.$on('dialog-close', function() {
            console.info("hook dialog-close", arguments);
            th.frameInst.$destroy()
            th.dialogInst.$destroy()
        })
        th.dialogInst.$mount()

        th.dialogInst.showDialog()

        console.info("frameInst", th.frameInst);
    }

    closeFrame(cmd) {
        console.info("shower close frame with cmd", cmd);
        this.dialogInst.hideDialog()
    }

}

function showDialog(params) {
    let shower = new ShowerDialog(params)
    shower.showFrame()
}

export {
    showDialog,
}
