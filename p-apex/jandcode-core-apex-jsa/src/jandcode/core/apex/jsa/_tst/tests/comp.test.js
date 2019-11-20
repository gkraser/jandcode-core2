import {apex, test} from './vendor'

let assert = test.assert

describe("comp.test.js", function() {

    test.initUi()

    it("1", function() {
        let vm = new Vue({
            render(h) {
                return h('q-btn', {props: {label: 'Ok1', color: 'primary'}})
            }
        })
        vm.$mount("#jc-app")
    })

    it("2", function() {
        let vm = new Vue({
            render(h) {
                return h('q-btn', {props: {label: 'Ok2', color: 'primary'}})
            }
        })
        vm.$mount("#jc-app")
    })

    it("3", function() {
        let Comp = {
            template: `<q-btn label="hello3"/>`
        }

        let vm = new Vue({
            render(h) {
                return h(Comp)
            }
        })
        vm.$mount("#jc-app")
    })

})

