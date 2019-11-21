import Page from './JcDecorFramePage'
import Dialog from './JcDecorFrameDialog'

/**
 * Базовый предок для фреймов
 */
export default {

    components: {
        Page: Page,
        Dialog: Dialog,
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
