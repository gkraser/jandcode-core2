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
            this.shower.closeFrame(cmd)
        }

    }

}
