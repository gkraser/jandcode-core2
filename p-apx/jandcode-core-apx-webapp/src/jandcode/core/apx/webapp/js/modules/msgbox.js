/* msgbox
----------------------------------------------------------------------------- */

import * as baseapp from '../baseapp'
import ShowMsg from './msgbox/ShowMsg'
import ShowYn from './msgbox/ShowYn'
import ShowError from './msgbox/ShowError'

export function showYn(msg, onYes) {
    baseapp.showDialog({
        frame: ShowYn,
        propsData: {
            text: msg
        },
        onYes: onYes
    })
}

export function showMsg(msg) {
    baseapp.showDialog({
        frame: ShowMsg,
        propsData: {
            text: msg
        }
    })
}

export function showError(err) {
    //todo сделать красиво показ ошбки
    baseapp.showDialog({
        frame: ShowError,
        propsData: {
            text: err
        }
    })
}
