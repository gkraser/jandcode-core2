/* Фреймы
----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import Dialog from './dialog/Dialog'

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
