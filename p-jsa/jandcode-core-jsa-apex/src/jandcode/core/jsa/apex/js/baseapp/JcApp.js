import App from '../components/decor/JcDecorAppStd'
import {componentHolder} from './frame'

/**
 * Базовый компонент-предок для приложения.
 */
export default {

    components: {
        App: App
    },

    created() {
        componentHolder.updateComponents(this, 'app')
    },

    data() {
        return {
            title: 'Без заголовка',
            icon: 'app-logo',

            left: true,
            leftWidth: 280,

            right: false,
            rightWidth: 280,
        }
    },

    methods: {
        home() {
            if (this.$router) {
                this.$router.push("/", () => {})
            }
        }
    }

}
