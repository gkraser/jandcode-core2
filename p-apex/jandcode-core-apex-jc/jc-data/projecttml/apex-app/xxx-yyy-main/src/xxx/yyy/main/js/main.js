import './index'
import App from './App'

export function run() {
    var vm = new Vue({
        render(h) {
            return h(App)
        }
    })
    vm.$mount("#jc-app")
}

