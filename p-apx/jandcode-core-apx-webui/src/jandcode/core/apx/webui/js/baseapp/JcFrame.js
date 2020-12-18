import Page from '../components/decor/JcDecorFramePage'
import Dialog from '../components/decor/JcDecorFrameDialog'
import {componentHolder} from './frame'
import {jsaBase} from '../vendor'

/**
 * Базовый предок для фреймов
 *
 * Свойство frameParams описывает параметры фрейма.
 *
 * Метод frameInit() инициализует фрейм. Выполняется после created и до mount.
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

        /**
         * @return {FrameWrapper}
         */
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
         * Типизированный доступ к параметрам фрейма.
         * @return {FrameParams}
         */
        frameParams: function() {
            if (this.__frameParams == null) {
                this.__frameParams = this.frameWrapper.createFrameParams()
            }
            return this.__frameParams
        },

        /**
         * Можно ли закрывать этот фрейм
         */
        isClosable() {
            return this.frameWrapper.isClosable()
        }

    },

    methods: {

        /**
         * showFrame из фрейма - автоматически помещается в стек
         */
        async showFrame(options) {
            if (!('stack' in options)) {
                options = jsaBase.extend({}, options, {stack: true})
            }
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
