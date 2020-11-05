import Page from '../components/decor/JcDecorFramePage'
import Dialog from '../components/decor/JcDecorFrameDialog'
import {componentHolder} from './frame'

/**
 * Базовый предок для фреймов
 */
export default {

    components: {
        Page: Page,
        Dialog: Dialog,
    },

    created() {
        componentHolder.updateComponents(this, 'frame')
        // менеджер фреймов поставит сюда себя
        this.frameManager = null
    },

    data() {
        return {
            title: null,
            title2: null,
            icon: null,
        }
    },

    methods: {

        /**
         * Закрыть фрейм с указанной командой.
         * @param cmd команда
         */
        closeFrame(cmd) {
            this.frameManager.closeFrame(this, cmd)
        }

    }

}
