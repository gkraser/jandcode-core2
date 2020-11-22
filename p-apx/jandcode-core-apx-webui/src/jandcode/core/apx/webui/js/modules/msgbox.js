/* msgbox
----------------------------------------------------------------------------- */

import * as baseapp from '../baseapp'
import ShowMsg from './msgbox/ShowMsg'
import ShowYn from './msgbox/ShowYn'
import ShowError from './msgbox/ShowError'

export async function showYn(msg, onYes) {
    return await baseapp.showDialog({
        frame: ShowYn,
        params: {
            text: msg
        },
        onYes: onYes
    })
}

export async function showMsg(msg) {
    return await baseapp.showDialog({
        frame: ShowMsg,
        params: {
            text: msg
        }
    })
}

export async function showError(err) {
    //todo сделать красиво показ ошбки
    return await baseapp.showDialog({
        frame: ShowError,
        params: {
            text: err
        }
    })
}
