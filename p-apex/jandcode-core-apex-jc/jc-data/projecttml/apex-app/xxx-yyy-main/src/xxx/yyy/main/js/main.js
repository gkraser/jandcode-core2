import {jsaBase} from './vendor'
import App from './App.vue'

export function run() {

    jsaBase.app.run(() => {
        let vm = new Vue({
            render(h) {
                return h(App)
            }
        })
        vm.$mount("#jc-app")
    })

}

