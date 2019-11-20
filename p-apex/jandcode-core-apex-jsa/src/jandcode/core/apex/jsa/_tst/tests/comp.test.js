import {apex, test} from './vendor'

describe("comp.test.js", function() {

    test.initUi()
    test.pauseAfterEach()

    it("3", function() {
        let Comp = {
            template: `<q-btn label="hello3"/>`
        }
        let vm = test.vueMount(Comp)
    })

})

