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

    computed: {

        frameWrapper: function() {
            let fw = this.$options.frameWrapper
            if (fw == null) {
                throw new Error("frameWrapper не установлен, видимо фрейм используется вне FrameManager")
            }
            return fw
        },

        /**
         * Параметры из showFrame
         */
        params: function() {
            return this.frameWrapper.params
        }

    },

    methods: {

        async showFrame(options) {
            await this.frameWrapper.frameManager.showFrame(options)
        },

        async showDialog(options) {
            await this.frameWrapper.frameManager.showDialog(options)
        },

        /**
         * Закрыть фрейм с указанной командой.
         * @param cmd команда
         */
        closeFrame(cmd) {
            this.frameWrapper.closeFrame(cmd)
        }

    }

}
