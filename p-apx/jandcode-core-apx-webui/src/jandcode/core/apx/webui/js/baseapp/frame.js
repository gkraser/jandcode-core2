/* Фреймы, запчасти
----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

import {ComponentHolder} from './component-holder'

// настройки темы по умолчанию
jsaBase.cfg.setDefault({
    theme: {
        dialog: {},
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
    'ok': {label: 'Ок', cmd: 'ok'},
    'cancel': {label: 'Отмена', cmd: 'cancel', kind: 'danger'},
    'close': {label: 'Закрыть', cmd: 'cancel'},
    'yes': {label: 'Да', cmd: 'yes'},
    'no': {label: 'Нет', cmd: 'no'},
    'save': {label: 'Сохранить', cmd: 'ok'},
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

