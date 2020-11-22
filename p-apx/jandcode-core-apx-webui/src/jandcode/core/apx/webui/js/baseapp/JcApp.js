import App from '../components/decor/JcDecorAppStd'
import {componentHolder} from './frame'
import {jsaBase} from '../vendor'

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
            title2: null,
            icon: 'app-logo',

            left: true,
            leftWidth: 280,

            right: false,
            rightWidth: 280,
        }
    },

    computed: {
        frameManager: function() {
            return jsaBase.app.frameManager
        },
    },

    methods: {

        async showFrame(options) {
            await this.frameManager.showFrame(options)
        },

        async showDialog(options) {
            await this.frameManager.showDialog(options)
        },

        home() {
            this.showFrame({frame: ''})
        }

    }

}
