import {apx, Vue} from './vendor'
import App from './App'
import Home from './frames/Home'

export function run() {

    let routes = [
        {path: '', frame: Home},
    ]

    apx.app.run(() => {

        apx.app.frameRouter.addRoutes(routes)

        let vm = new Vue({
            render(h) {
                return h(App)
            }
        })
        vm.$mount("#jc-app")

    })

}
