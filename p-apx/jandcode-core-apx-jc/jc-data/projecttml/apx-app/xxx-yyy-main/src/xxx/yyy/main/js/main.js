import {jsaBase, apx} from './vendor'
import App from './App'
import Home from './frames/Home'

export function run() {

    let routes = [
        {path: '/', redirect: '/home'},
        {path: '/home', component: Home},
        {path: '*', component: apx.components.JcPage404},
    ]

    jsaBase.app.run(() => {

        let router = new VueRouter({
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
