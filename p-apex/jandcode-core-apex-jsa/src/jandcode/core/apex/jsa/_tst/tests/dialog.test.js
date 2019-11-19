import * as test from './vendor'

import * as apex from '../../js'

let assert = test.assert

describe(__filename, function() {

    function makeFrame(params) {
        let Comp = {
            extends: apex.JcFrame,
            template: `<Dialog>1</Dialog>`
        }
        Comp = apex.extend(Comp, params)
        return Comp
    }

    function showDialog(frameParams, showParams) {
        let FrameComp = makeFrame(frameParams)
        showParams = Object.assign({}, showParams)
        showParams.frame = FrameComp
        return apex.frame.showDialog(showParams)
    }

    it("1", function(cb) {

        let FrameComp = makeFrame()
        let frame = apex.frame.showDialog({frame: FrameComp})

        setTimeout(() => {
            frame.closeFrame()
            cb()
        }, 200)

    })

    it("onOk in frame", function(cb) {

        let FrameComp = makeFrame({
            onOk: function() {
                console.info("onOk");
            }
        })
        let frame = apex.frame.showDialog({
            frame: FrameComp
        })

        setTimeout(() => {
            frame.closeFrame('ok')
            cb()
        }, 200)

    })

    it("onOk in frame 2", function(cb) {

        let frame = showDialog({
            onOk: function() {

            }
        }, {})

        setTimeout(() => {
            frame.closeFrame('ok')
            cb()
        }, 200)

    })


})

