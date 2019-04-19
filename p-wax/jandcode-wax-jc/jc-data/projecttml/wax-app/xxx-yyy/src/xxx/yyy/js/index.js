//
import {jcWax} from './vendor'
import Main from './Main'

//
export function run() {
    let vm = new Vue({
        render(h) {
            return h(Main)
        }
    })
    vm.$mount("#jc-app")
}
