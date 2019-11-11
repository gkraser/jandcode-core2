import './vendor'
import App from './App'

import Main from './pages/Main'

export function run() {

    const routes = [
        {path: '/', component: Main},
    ]

    const router = new VueRouter({
        routes: routes
    })

    let vm = new Vue({
        router: router,
        render(h) {
            return h(App)
        }
    })
    vm.$mount("#jc-app")

}

