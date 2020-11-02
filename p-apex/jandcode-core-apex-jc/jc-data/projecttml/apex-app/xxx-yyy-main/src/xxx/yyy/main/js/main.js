import {jsaBase, apex} from './vendor'
import App from './App'
import Home from './frames/Home'

export function run() {

    let routes = [
        {path: '/', redirect: '/home'},
        {path: '/home', component: Home},
        {path: '*', component: apex.components.JcPage404},
    ]

    jsaBase.app.run(() => {

        const router = new VueRouter({
            routes: routes,
        })

        let vm = new Vue({
            router: router,
            render(h) {
                return h(App)
            }
        })
        vm.$mount("#jc-app")

    })

}
