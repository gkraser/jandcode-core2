/* Показ wait
----------------------------------------------------------------------------- */

import {Vue, Quasar, jsaBase} from '../../vendor'

class ApxWaitUI extends jsaBase.WaitUI {

    show() {
        let loadingProps = {
            delay: 0,
            spinner: Quasar.QSpinnerGears,
            spinnerColor: 'deep-orange-6',
            backgroundColor: 'white',
        }
        let loadingProps2 = jsaBase.extend({}, loadingProps, {backgroundColor: 'black'})
        Quasar.Loading.show(loadingProps)
        this.timerId = setTimeout(() => {
            Quasar.Loading.show(loadingProps2)
        }, 2000)
    }

    hide() {
        if (this.timerId) {
            window.clearTimeout(this.timerId)
            this.timerId = null
        }
        Quasar.Loading.hide()
    }

}

export class WaitUIService extends jsaBase.AppService {

    onBeforeRun() {
        this.waitUI = new ApxWaitUI()
        jsaBase.setWaitUI(this.waitUI)
    }

    onStop() {
        jsaBase.setWaitUI(null)
    }

}


