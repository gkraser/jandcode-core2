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
        },

        /**
         * Можно ли закрывать этот фрейм
         */
        isClosable() {
            return this.frameWrapper.isClosable()
        }

    },

    methods: {

        async showFrame(options) {
            return await this.frameWrapper.frameManager.showFrame(options)
        },

        async showDialog(options) {
            return await this.frameWrapper.frameManager.showDialog(options)
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
