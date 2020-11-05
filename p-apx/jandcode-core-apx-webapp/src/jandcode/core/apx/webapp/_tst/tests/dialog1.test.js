import {apx, jsaBase, test} from '../vendor'

describe(__filename, function() {

    test.pauseAfterEach()

    function makeFrame(params) {
        let Comp = {
            extends: apx.JcFrame,
            template: `<Dialog>1</Dialog>`
        }
        Comp = jsaBase.extend(Comp, params)
        return Comp
    }

    function showDialog(frameParams, showParams) {
        let FrameComp = makeFrame(frameParams)
        showParams = Object.assign({}, showParams)
        showParams.frame = FrameComp
        return apx.showDialog(showParams)
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
        }, 2)

    })

    it("initFrame1", function(cb) {
        let s = ''

        let Comp1 = {
            extends: apx.JcFrame,
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
            frame.closeFrame('ok')
            test.assert.equal(s, '012')
            cb()
        }, 1)

    })

    it("props-data", async function() {
        let frame = showDialog({
            props: {
                p1: {
                    default: '1'
                }
            }
        }, {
            propsData: {
                p1: '123'
            }
        })

        test.assert.equal(frame.p1, '123')

        frame.closeFrame('ok')
    })

})

