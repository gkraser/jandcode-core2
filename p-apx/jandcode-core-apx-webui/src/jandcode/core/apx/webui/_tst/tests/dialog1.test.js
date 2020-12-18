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

    async function showDialog(frameParams, showParams) {
        let FrameComp = makeFrame(frameParams)
        showParams = Object.assign({}, showParams)
        showParams.frame = FrameComp
        let f = await apx.showDialog(showParams)
        return f
    }

    it("onOk", async function() {
        let s = ''

        let frame = await showDialog({
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
            setTimeout(() => {
                test.assert.equal(s, '12')
            }, 2)
        }, 2)

    })

    it("frameInit1", async function() {
        let s = ''

        let Comp1 = {
            extends: apx.JcFrame,
            template: '<div></div>',
            created() {
                this.tag1 = true // маркер this
            },
            async frameInit() {
                test.assert.ok(this.tag1)
                await new Promise(function(resolve) {
                    s += '0'
                    resolve()
                })
                s += '1'
            }
        }

        let frame = await showDialog({
            extends: Comp1,
            frameInit() {
                test.assert.ok(this.tag1)
                s += '2'
            }
        }, {})

        setTimeout(() => {
            frame.closeFrame('ok')
            test.assert.equal(s, '012')
        }, 1)

    })

    it("props-data", async function() {
        let frame = await showDialog({
            created() {
                this.p1 = this.params.p1
            },
        }, {
            params: {
                p1: '123'
            }
        })

        setTimeout(() => {
            frame.closeFrame('ok')
            test.assert.equal(frame.p1, '123')
        }, 1)

    })

})

