import {apex, test} from './vendor'

describe("JcSideMenu.test.js", function() {

    test.initUi()
    test.pauseAfterEach()

    it("1", function() {
        let vm = test.vueMount({
            template: `<jc-side-menu-item ref="r1" label="z1" icon="bus"/>`
        })
        test.assert.equal(vm.$refs.r1.label, 'z1')
    })

})

