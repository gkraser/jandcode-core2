import {apex, test} from './vendor'

describe("dialog.test.js", function() {

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
        console.info("showParams", showParams);
        return apex.frame.showDialog(showParams)
    }

    it("onOk", function(cb) {
        let s = ''

        let frame = showDialog({
            methods: {
                onOk: function() {
                    s += '1'
                }
            }
        }, {
            onOk: function() {
                s += '2'
            }
        })

        setTimeout(() => {
            frame.closeFrame('ok')
            test.assert.equal(s, '12')
            cb()
        }, 1)

    })

    it("initFrame1", function(cb) {
        let s = ''

        let Comp1 = {
            extends: apex.JcFrame,
            template: '<div></div>',
            created() {
                this.tag1 = true // маркер this
            },
            async initFrame() {
                test.assert.ok(this.tag1)
                await new Promise(function(resolve) {
                    s += '0'
                    resolve()
                })
                s += '1'
            }
        }

        let frame = showDialog({
            extends: Comp1,
            initFrame() {
                test.assert.ok(this.tag1)
                s += '2'
            }
        }, {})

        setTimeout(() => {
            console.info("frame", frame);
            frame.closeFrame('ok')
            test.assert.equal(s, '012')
            cb()
        }, 1)

    })


})

